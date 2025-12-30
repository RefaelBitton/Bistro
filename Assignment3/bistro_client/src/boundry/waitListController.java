package boundry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import entities.JoinWaitlistRequest;
import entities.User;
import entities.UserType;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class waitListController implements IController {
    private User user;
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    @FXML private TextField guestsNumberTxt;
    @FXML private TextField identifyText; // This serves as the Contact field for Guests
    @FXML private Label idLabel;
    @FXML private TextArea resultTxt;
    @FXML private Button backBtn;
    @FXML private Button submitBtn;

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
        
        // Requirement: Hide identification fields for logged-in Subscribers
        identifyText.visibleProperty().bind(isLoggedIn.not());
        identifyText.managedProperty().bind(isLoggedIn.not());
        idLabel.visibleProperty().bind(isLoggedIn.not());
        idLabel.managedProperty().bind(isLoggedIn.not());
    }

    @FXML
    public void onJoinWaitlistClick(ActionEvent event) {
        String guests = guestsNumberTxt.getText().trim();
        // Use identifyText since it matches your FXML "Email / Phone Number" field
        String contactInput = identifyText.getText().trim(); 

        // 1. Validate Guest Count
        if (guests.isEmpty() || !guests.matches("\\d+") || Integer.parseInt(guests) <= 0) {
            resultTxt.setText("Please enter a valid number of guests.");
            return;
        }
        
        // 2. Validate Contact (Required only for Guests)
        if (user.getType() == UserType.GUEST) {
            if (contactInput.isEmpty() || !isValidPhoneOrEmail(contactInput)) {
                resultTxt.setText("❌ Please enter a valid contact (phone or email).");
                return;
            }
        }

        // 3. Prepare data for JoinWaitlistRequest
        String orderDateTime = LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8);
        String subscriberId = (user.getType() == UserType.SUBSCRIBER) ? 
                             String.valueOf(((entities.Subscriber)user).getSubscriberID()) : "0";
        String finalContact = (user.getType() == UserType.SUBSCRIBER && contactInput.isEmpty()) ? 
                             user.getPhone() : contactInput;

        // 4. Send Request
        ClientUI.console.accept(new JoinWaitlistRequest(
            orderDateTime, guests, subscriberId, finalContact
        ));
    }

    /** Helper for contact validation */
    private boolean isValidPhoneOrEmail(String input) {
        return input.matches("^\\d{10}$") || input.contains("@");
    }

    @Override
    public void setResultText(Object result) {
        String message = (String) result;
        
        // Requirement: Show Popup if no table is found immediately
        if (message.contains("No immediate seating")) {
            Platform.runLater(this::showWaitlistConfirmPopup);
        } else {
            resultTxt.setText(message);
        }
    }

    private void showWaitlistConfirmPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Waitlist Selection");
        alert.setHeaderText("No open spots found.");
        alert.setContentText("Would you like to enter the waitlist or cancel?");

        ButtonType btnJoin = new ButtonType("Join Waitlist");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnJoin, btnCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnJoin) {
            resultTxt.setText("⏳ Adding you to the waitlist...");
            // Logic to resend with waitlist flag if your server requires it
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
    }

    @FXML
    void OnBackBtnClick(ActionEvent event) {
        try {
            ClientUI.console.switchScreen(this, event, "/boundry/TerminalScreen.fxml", user);
        } catch (Exception e) {
            resultTxt.setText("Error switching screens.");
        }
    }
}