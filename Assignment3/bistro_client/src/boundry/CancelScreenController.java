package boundry;
//Logic Moved to AppOrderManagementController.java
import java.io.IOException;
import java.util.Optional;

import entities.CancelRequest;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for the Cancel Screen. Handles user interactions for
 * canceling orders.
 */
public class CancelScreenController implements IController{
	private User user;
	
	@FXML
	private TextField orderNumTxt;
	
	@FXML
	private TextField confCodeTxt;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button backBtn;

	/**
	 * Handles the cancel button click event. Validates input and sends a cancel
	 * request if confirmed by the user.
	 * 
	 * @param event The action event triggered by clicking the cancel button.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@FXML
	void onCancelClick(ActionEvent event) throws IOException, InterruptedException{
		boolean exceptionRaised = false;
    	int orderNum = 0;
    	int code = 0;
    	try {
    		//parsing integers fields
    		orderNum = Integer.parseInt(orderNumTxt.getText().trim());
    		code = Integer.parseInt(confCodeTxt.getText().trim());
    		if(orderNum <= 0 || code <=0) {
    			exceptionRaised = true;
    		}
    	}catch (NumberFormatException e) { 
			exceptionRaised = true;
			orderNumTxt.clear();
			confCodeTxt.clear();
    	}
    	if(exceptionRaised) {
    		Alert alert = new Alert(AlertType.ERROR);
    	    alert.setTitle("Error Occurred");
    	    alert.setHeaderText("Input Validation Failed");
    	    alert.setContentText("you cannot enter non-positive number");
    	    alert.showAndWait();
    	}
    	else {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Confirmation");
        	alert.setHeaderText("Your order will be deleted");
        	alert.setContentText("Are you sure you want to continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.isPresent() && result.get() == ButtonType.OK) {
        		CancelRequest c = new CancelRequest(confCodeTxt.getText().trim());
            	ClientUI.console.accept(c);
        	}
    	}
	}

	/**
	 * 	Handles the back button click event. Navigates back to the main screen.
	 * @param event
	 */
    @FXML
    void OnBackBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml",user);
    }

	/**
	 * Sets the result text. (Not used in this controller)
	 * 
	 * @param result The result object to set.
	 */
	public void setResultText(Object result) {
		return;
	}

	/**
	 * Sets the user for this controller.
	 */
    public void setUser(User user) {
    	this.user = user;
    }
}
