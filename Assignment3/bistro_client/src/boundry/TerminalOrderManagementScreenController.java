package boundry;

import java.util.Optional;
import entities.CheckConfCodeRequest;
import entities.GetTableRequest;
import entities.User;
import entities.UserType;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar;

/**
 * Controller class for managing orders in the application.
 */
public class TerminalOrderManagementScreenController implements IController {
	private User user;
	private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    @FXML
    private Button backBtn;

    @FXML
    private Button lostMyCodeBtn;

    @FXML
    private TextField confCodeTxt;

    @FXML
    private Button leaveTableBtn;

    @FXML
    private Button getTableBtn;
    
    @FXML
    void initialize() {
    	ClientUI.console.setController(this);
    }
   
    
    /**
	 * Handles the action when the back button is clicked.
	 * Navigates to the appropriate screen based on user type.
	 * 
	 * @param event The action event triggered by clicking the back button.
	 */
    @FXML
    void onbackClick(ActionEvent event) {
			ClientUI.console.switchScreen(this, event, "/boundry/TerminalScreen.fxml", user);
		}
	
    /**
	 * Handles the action when the cancel order button is clicked.
	 * Validates input and sends a cancel request if confirmed.
	 * @param event The action event triggered by clicking the cancel order button.
	 */
    @FXML
    void onLostMyCodeClick(ActionEvent event) {

        String contact;

        // ---------- GUEST ----------
        if (!isLoggedIn.get()) {

            TextField contactField = new TextField();
            contactField.setPromptText("Enter phone or email used in the order");

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Find Your Order");
            dialog.setHeaderText("Please enter your contact");

            ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmBtn, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(contactField);

            dialog.setResultConverter(btn -> {
                if (btn == confirmBtn) {
                    return contactField.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();

            // user cancelled popup
            if (result.isEmpty()) return;

            contact = result.get().trim();

            if (contact.isEmpty() || !OrderScreenController.isValidPhoneOrEmail(contact)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText("Invalid contact");
                alert.setContentText(
                    "Please enter a valid phone number or email\n" +
                    "that you used when placing the order."
                );
                alert.showAndWait();
                return;
            }
        }

        // ---------- SUBSCRIBER ----------
        else {
            contact = user.getEmail();
        }

        // ---------- SEND REQUEST ----------
        ClientUI.console.accept(new CheckConfCodeRequest(contact));
    }


    @FXML
    void onLeaveTableClick(ActionEvent event) {
    	//navigate to finish order screen (not implemented yet)
    	ClientUI.console.switchScreen(this, event, "/boundry/FinishOrderScreen.fxml", user);

    }

    @FXML
    void onGetTableClick(ActionEvent event) {
    	String confcode = confCodeTxt.getText().trim();
    	if (confcode.isEmpty() ||  !confcode.matches("\\d+")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Occurred");
			alert.setContentText("Please enter a valid confirmation code.");
			alert.showAndWait();
			return;
		}
    	ClientUI.console.accept(new GetTableRequest(confcode));
    	
    	

    }

	@Override
	public void setResultText(Object result) {
		Platform.runLater(() -> 
		{Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Occurred");
        alert.setHeaderText("Invalid contact");
	    alert.setContentText((String) result);
	    alert.showAndWait();
		}
	    );
	}

	@Override
	public void setUser(User user) {
		this.user = user;	
		isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
	}

}

	


