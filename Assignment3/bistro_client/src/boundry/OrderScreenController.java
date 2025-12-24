// ======================= boundry/OrderScreenController.java =======================
package boundry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entities.Order;
import entities.ReadEmailRequest;
import entities.Subscriber;
import entities.User;
import entities.UserType;
import entities.WriteRequest;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class OrderScreenController implements IController {

    private User user;

    @FXML private DatePicker orderDatePicker;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private ComboBox<Integer> guestsComboBox;
    @FXML private TextArea resultTxt;
    @FXML private Button OrderBtn;
    @FXML private Button cancelBtn;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // ✅ NEW guest contact UI
    @FXML private HBox contactBox;
    @FXML private TextField contactTxt;

    // subscriber logged in => true, guest => false
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    // subscriber order flow: first READ_EMAIL then WRITE_ORDER
    private boolean waitingForEmail = false;
    private LocalDate pendingDate;
    private String pendingTime;
    private Integer pendingGuests;
    private int pendingSubscriberId;

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);

        // ✅ exactly in your requested format
        contactBox.visibleProperty().bind(isLoggedIn.not());
        contactBox.managedProperty().bind(isLoggedIn.not());

        // Guests: 1–20
        for (int i = 1; i <= 20; i++) {
            guestsComboBox.getItems().add(i);
        }

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusMonths(1);

        // Restrict date picker
        orderDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(today) || date.isAfter(maxDate)) {
                    setDisable(true);
                }
            }
        });

        // Update time options when date changes
        orderDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            updateAvailableTimes(newDate);
        });
    }

    private void updateAvailableTimes(LocalDate selectedDate) {
        timeComboBox.getItems().clear();
        if (selectedDate == null) return;

        LocalTime opening = LocalTime.of(11, 0);
        LocalTime closing = LocalTime.of(22, 0);
        LocalDateTime nowPlusHour = LocalDateTime.now().plusHours(1);

        for (LocalTime time = opening; time.isBefore(closing); time = time.plusMinutes(30)) {
            LocalDateTime candidate = LocalDateTime.of(selectedDate, time);

            // Only restrict time if the date is today
            if (selectedDate.equals(LocalDate.now())) {
                if (candidate.isBefore(nowPlusHour)) continue;
            }

            timeComboBox.getItems().add(time.toString());
        }
    }

    @FXML
    void OnOrderClick(ActionEvent event) {
        resultTxt.clear();

        try {
            LocalDateTime date = LocalDateTime.parse(orderDatePicker.getValue().toString(),dateTimeFormatter);
            String time = timeComboBox.getValue();
            Integer guests = guestsComboBox.getValue();

            if (date == null || time == null || guests == null) {
                throw new IllegalArgumentException();
            }
            // Guard: working hours 11:00 <= time < 22:00 (matches your client)
            if(!isWithinWorkingHours(date.toLocalTime())) {
            	
                resultTxt.setText("❌ Selected time is outside working hours (11:00–22:00).");
            }

            // GUEST: must provide contact (phone OR email)
            if (!isLoggedIn.get()) {
                String contact = (contactTxt.getText() == null) ? "" : contactTxt.getText().trim();
                if (contact.isEmpty() || !isValidPhoneOrEmail(contact)) {
                    resultTxt.setText("❌ Guest must enter a valid phone OR email.");
                    return;
                }

                ArrayList<String> args = new ArrayList<>();
                args.add(date.toString());        // 0: order date
                args.add(time);                   // 1: time (UI only; DB may ignore)
                args.add(guests.toString());      // 2: guests
                args.add("0");                    // 3: subscriberId=0 (server converts to NULL)
                args.add(contact);                // 4: contact

                ClientUI.console.accept(new WriteRequest(new Order(args)));
                return;
            }

            // SUBSCRIBER: first READ_EMAIL, then WRITE_ORDER
            int subId = ((Subscriber) user).getSubscriberID();

            waitingForEmail = true;
            pendingDate = date;
            pendingTime = time;
            pendingGuests = guests;
            pendingSubscriberId = subId;

            resultTxt.setText("Fetching subscriber email...");
            ClientUI.console.accept(new ReadEmailRequest(subId));

        } catch (Exception e) {
            resultTxt.setText(
                "❌ Invalid input.\n\n" +
                "• Select a valid date (today – 1 month ahead)\n" +
                "• If today: select time at least 1 hour from now\n" +
                "• Select number of guests (1–20)\n" +
                "• If guest: enter phone OR email"
            );
        }
    }

    private boolean isValidPhoneOrEmail(String s) {
        boolean looksEmail = s.contains("@") && s.contains(".") && s.indexOf('@') > 0;
        String digits = s.replaceAll("[^0-9]", "");
        boolean looksPhone = digits.length() >= 9 && digits.length() <= 15;
        return looksEmail || looksPhone;
    }

    @FXML
    void onCancelClick(ActionEvent event) throws IOException {
        ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml", user);
    }

    @Override
    public void setUser(User user) {
    	System.out.println("OrderScreen setUser() called. user=" + user +
                " type=" + (user == null ? "null" : user.getType()));
        this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
    }

    @Override
    public void setResultText(String result) {

        // If we are waiting for the subscriber email, parse it here
        if (waitingForEmail) {

            if (result != null && result.startsWith("EMAIL:")) {
                String email = result.substring("EMAIL:".length()).trim();

                waitingForEmail = false;

                if (email.isEmpty()) {
                    resultTxt.setText("❌ Subscriber email not found in DB.");
                    return;
                }

                ArrayList<String> args = new ArrayList<>();
                args.add(pendingDate.toString());
                args.add(pendingTime);
                args.add(pendingGuests.toString());
                args.add(String.valueOf(pendingSubscriberId));
                args.add(email); // ✅ contact = subscriber email

                resultTxt.setText("Placing order...");
                ClientUI.console.accept(new WriteRequest(new Order(args)));
                return;
            }

            if (result != null && result.startsWith("EMAIL_ERROR:")) {
                waitingForEmail = false;
                resultTxt.setText("❌ Error reading email:\n" + result);
                return;
            }

            waitingForEmail = false;
            resultTxt.setText("❌ Unexpected response:\n" + result);
            return;
        }

        // Normal responses (order saved / validation / etc.)
        resultTxt.setText(result);
    }
    // matches your client time generation: 11:00 <= t < 22:00
    private boolean isWithinWorkingHours(LocalTime time) {
        LocalTime opening = LocalTime.of(11, 0);
        LocalTime closing = LocalTime.of(22, 0);
        return !time.isBefore(opening) && time.isBefore(closing);
    }
}
