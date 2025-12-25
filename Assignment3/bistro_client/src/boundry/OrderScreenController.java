package boundry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

import entities.CheckSlotRequest;
import entities.OrderNumberRequest;
import entities.ShowOpenSlotsRequest;
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


    //  NEW guest contact UI
    @FXML private HBox contactBox;
    @FXML private TextField contactTxt;

    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    private boolean waitingForEmail = false;
    private boolean waitingSlotCheck = false;
    private boolean waitingTakenList = false;
    private boolean waitingNextOrderNumber = false;

    private LocalDate pendingDate;
    private String pendingTime;
    private Integer pendingGuests;
    private String pendingSubscriberIdStr;
    private String pendingContact;
    private String pendingOrderDateTime;

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);

        contactBox.visibleProperty().bind(isLoggedIn.not());
        contactBox.managedProperty().bind(isLoggedIn.not());

        for (int i = 1; i <= 20; i++) guestsComboBox.getItems().add(i);

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusMonths(1);

        orderDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(today) || date.isAfter(maxDate)) setDisable(true);
            }
        });

        orderDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> updateAvailableTimes(newDate));
    }

    private void updateAvailableTimes(LocalDate selectedDate) {
        timeComboBox.getItems().clear();
        if (selectedDate == null) return;

        LocalTime opening = LocalTime.of(11, 0);
        LocalTime closing = LocalTime.of(22, 0);
        LocalDateTime nowPlusHour = LocalDateTime.now().plusHours(1);

        for (LocalTime t = opening; t.isBefore(closing); t = t.plusMinutes(30)) {
            LocalDateTime candidate = LocalDateTime.of(selectedDate, t);
            if (selectedDate.equals(LocalDate.now()) && candidate.isBefore(nowPlusHour)) continue;
            timeComboBox.getItems().add(t.toString()); // HH:mm
        }
    }

    @FXML
    void OnOrderClick(ActionEvent event) {
        resultTxt.clear();

        try {
            LocalDate date = orderDatePicker.getValue();
            String time = timeComboBox.getValue();
            Integer guests = guestsComboBox.getValue();
            if (date == null || time == null || guests == null) throw new IllegalArgumentException();

            LocalTime chosen = LocalTime.parse(time);
            if (chosen.isBefore(LocalTime.of(11,0)) || !chosen.isBefore(LocalTime.of(22,0))) {
                resultTxt.setText("❌ Selected time is outside working hours (11:00–22:00).");
                return;
            }
            
            if (date.equals(LocalDate.now())) {
                if (LocalDateTime.of(date, chosen).isBefore(LocalDateTime.now().plusHours(1))) {
                    resultTxt.setText("❌ If ordering today, choose a time at least 1 hour from now.");
                    return;
                }
            }

            pendingDate = date;
            pendingTime = time;
            pendingGuests = guests;
            pendingOrderDateTime = date + " " + time + ":00";

            if (!isLoggedIn.get()) {
                String contact = (contactTxt.getText() == null) ? "" : contactTxt.getText().trim();
                if (contact.isEmpty() || !isValidPhoneOrEmail(contact)) {
                    resultTxt.setText("❌ Guest must enter a valid phone OR email.");
                    return;
                }
                pendingSubscriberIdStr = "0";
                pendingContact = contact;

                waitingSlotCheck = true;
                resultTxt.setText("Checking availability...");
                ClientUI.console.accept(new CheckSlotRequest(pendingOrderDateTime));
                return;
            }

            int subId = ((Subscriber) user).getSubscriberID();
            waitingForEmail = true;
            resultTxt.setText("Fetching subscriber email...");
            ClientUI.console.accept(new ReadEmailRequest(subId));

        } catch (Exception e) {
            resultTxt.setText("❌ Invalid input.");
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
        this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
    }

    private LocalDateTime parseDT(String s) {
        return LocalDateTime.parse(s, DT_FMT);
    }

    private LocalDateTime roundUpToNext30(LocalDateTime t) {
        int m = t.getMinute();
        int add = (m % 30 == 0) ? 0 : (30 - (m % 30));
        LocalDateTime r = t.plusMinutes(add).withSecond(0).withNano(0);
        return r;
    }

    private String buildSuggestions(LocalDate date, LocalDateTime requested, HashSet<String> taken) {
        LocalDateTime open = LocalDateTime.of(date, LocalTime.of(11,0));
        LocalDateTime last = LocalDateTime.of(date, LocalTime.of(21,30));

        LocalDateTime from = requested.minusHours(2);
        LocalDateTime to   = requested.plusHours(2);

        if (from.isBefore(open)) from = open;
        if (to.isAfter(last)) to = last;

        if (date.equals(LocalDate.now())) {
            LocalDateTime minToday = roundUpToNext30(LocalDateTime.now().plusHours(1));
            if (from.isBefore(minToday)) from = minToday;
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (LocalDateTime t = from; !t.isAfter(to); t = t.plusMinutes(30)) {
            String key = t.format(DT_FMT);
            if (!taken.contains(key)) {
                sb.append("• ").append(key.substring(0,16)).append("\n"); // yyyy-MM-dd HH:mm
                if (++count == 16) break;
            }
        }
        return sb.toString();
    }

    @Override
    public void setResultText(String result) {

        // 1) email -> then check slot
        if (waitingForEmail) {
            waitingForEmail = false;

            if (result != null && result.startsWith("EMAIL:")) {
                String email = result.substring("EMAIL:".length()).trim();
                if (email.isEmpty()) {
                    resultTxt.setText("❌ Subscriber email not found in DB.");
                    return;
                }
                pendingSubscriberIdStr = String.valueOf(((Subscriber) user).getSubscriberID());
                pendingContact = email;

                waitingSlotCheck = true;
                resultTxt.setText("Checking availability...");
                ClientUI.console.accept(new CheckSlotRequest(pendingOrderDateTime));
                return;
            }

            resultTxt.setText("❌ Unexpected response:\n" + result);
            return;
        }

        // 2) slot check
        if (waitingSlotCheck) {
            waitingSlotCheck = false;

            if ("FREE".equals(result)) {
                waitingNextOrderNumber = true;
                resultTxt.setText("Generating order number...");
                ClientUI.console.accept(new OrderNumberRequest());
                return;
            }

            if ("TAKEN".equals(result)) {
                LocalDateTime requested = parseDT(pendingOrderDateTime);

                LocalDateTime open = LocalDateTime.of(pendingDate, LocalTime.of(11,0));
                LocalDateTime last = LocalDateTime.of(pendingDate, LocalTime.of(21,30));

                LocalDateTime from = requested.minusHours(2);
                LocalDateTime to   = requested.plusHours(2);

                if (from.isBefore(open)) from = open;
                if (to.isAfter(last)) to = last;

                if (pendingDate.equals(LocalDate.now())) {
                    LocalDateTime minToday = roundUpToNext30(LocalDateTime.now().plusHours(1));
                    if (from.isBefore(minToday)) from = minToday;
                }

                waitingTakenList = true;
                ClientUI.console.accept(new ShowOpenSlotsRequest(from.format(DT_FMT), to.format(DT_FMT)));
                return;
            }

            resultTxt.setText("❌ Unexpected response:\n" + result);
            return;
        }

        // 3) taken list -> suggestions (THIS IS WHERE YOUR BUG WAS: parsing/clamping)
        if (waitingTakenList) {
            waitingTakenList = false;

            HashSet<String> taken = new HashSet<>();
            if (result != null) {
                for (String line : result.split("\n")) {
                    line = line.trim();
                    taken.add(line.substring(0, 19));
                }
            }

            LocalDateTime requested = parseDT(pendingOrderDateTime);
            String suggestions = buildSuggestions(pendingDate, requested, taken);

            if (suggestions.isEmpty()) {
                resultTxt.setText("❌ This date and time is already taken.\nNo available slots found within ±2 hours.");
            } else {
                resultTxt.setText("❌ This date and time is already taken.\nAvailable slots (±2 hours):\n" + suggestions);
            }
            return;
        }

        // 4) next order number -> build Order (with both numbers) -> WRITE
        if (waitingNextOrderNumber) {
            waitingNextOrderNumber = false;

            if (result != null && result.startsWith("NEXT_ORDER_NUMBER:")) {
                int orderNum = Integer.parseInt(result.substring("NEXT_ORDER_NUMBER:".length()).trim());
                int confCode = 100000 + (orderNum);

                ArrayList<String> args = new ArrayList<>();
                args.add(String.valueOf(orderNum));
                args.add(pendingDate.toString());
                args.add(pendingTime);
                args.add(String.valueOf(pendingGuests));
                args.add(String.valueOf(confCode));
                args.add(pendingSubscriberIdStr);
                args.add(pendingContact);
                Order order = new Order(args);

                resultTxt.setText("Placing order...");
                ClientUI.console.accept(new WriteRequest(order));
                return;
            }

            resultTxt.setText("❌ Failed to generate order number.\n" + result);
            return;
        }

        // 5) write result
        if (result != null && result.startsWith("INTEGRITY_ERROR:")) {
            resultTxt.setText("❌ Database constraint error:\n" + result);
            return;
        }

        resultTxt.setText(result);
    }
}
