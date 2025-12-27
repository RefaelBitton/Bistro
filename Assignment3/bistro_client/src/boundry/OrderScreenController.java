package boundry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import entities.ReserveRequest;
import entities.User;
import entities.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import entities.Subscriber;

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
    private Integer pendingGuests;
    private String pendingSubscriberIdStr;
    private String pendingContact;
    private String pendingOrderDateTime;
               
    @FXML private Button orderBtn;
    @FXML private Button backBtn;

    
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
        	//*====================================== VALIDATIONS =============================*//
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

                
                
                
            }else {
            	pendingSubscriberIdStr = String.valueOf(((Subscriber)user).getSubscriberID());
				pendingContact = ((Subscriber)user).getEmail();
            }
            
            ClientUI.console.accept(new ReserveRequest(pendingOrderDateTime, pendingGuests+"", pendingSubscriberIdStr, pendingContact));


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
    void onBackClick(ActionEvent event) throws IOException {
    	if(user.getType() == UserType.GUEST) {
            ClientUI.console.switchScreen(this, event, "/boundry/TerminalScreen.fxml", user);
    	}
    	else if(user.getType() == UserType.SUBSCRIBER) {
    		ClientUI.console.switchScreen(this, event, "/boundry/ClientScreen.fxml", user);
    	}
    	else {
    		ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
    	}
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
    }


    @Override
    public void setResultText(Object result) {
    	System.out.println("OrderScreenController received result: " + result);
    	String res = (String) result;
    	resultTxt.clear();
    	resultTxt.setText(res);
		}
    }
