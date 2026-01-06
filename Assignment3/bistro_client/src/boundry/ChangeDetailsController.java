package boundry;

import entities.Subscriber;
import entities.UpdateDetailsRequest;
import entities.User;
import entities.UserType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChangeDetailsController implements IController {
	private Subscriber user;
    @FXML
    private Button backBtn;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField fullNameTxt;

    @FXML
    private TextField phoneNumberTxt;

    @FXML
    private Button updateBtn;

    @FXML
    private TextField userNameTxt;

    @FXML
    void initialize() {
    	System.out.println("initialize ChangeDetailsController");
    	ClientUI.console.setController(this);
    }
    @FXML
    void onBackBtnClick(ActionEvent event) {
    	if(user.getType() ==UserType.SUBSCRIBER) {
    		ClientUI.console.switchScreen(this, event, "/boundry/ClientScreen.fxml", user);
    	}
    	else {
			ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
		}
    }
    
    @FXML
    void onUpdateClick(ActionEvent event) {
    	boolean emailException = false;
    	boolean phoneException = false;
    	String query = "UPDATE `user` SET";
    	if(!fullNameTxt.getText().isEmpty()) {
			query += " full_name = '" + fullNameTxt.getText() + "',";
		}
    	if(!emailTxt.getText().isEmpty()) {
    		if(!isValidEmail(emailTxt.getText()))
    			emailException = true;
    		else
    			query += " email = '" + emailTxt.getText() + "',";
    	}
    	if(!phoneNumberTxt.getText().isEmpty()) {
    		if(phoneNumberTxt.getText().length() != 10)
    			phoneException = true;
    		else
    			query += " phone_number = '" + phoneNumberTxt.getText() + "',";
    	}
    	if(!userNameTxt.getText().isEmpty()) {
			query += " username = '" + userNameTxt.getText() + "',";
		}
    	if(emailException) {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error Occurred");
    		alert.setHeaderText("Input Validation Failed");
    		alert.setContentText("Please enter valid email");
    		alert.showAndWait();
    	}
    	if(phoneException) {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error Occurred");
    		alert.setHeaderText("Input Validation Failed");
    		alert.setContentText("Please enter valid phone number with 10 digits");
    		alert.showAndWait();
    	}
    	if((!emailException) && (!phoneException)) {
        	query = query.substring(0, query.length() - 1); // remove last comma
        	query += " WHERE subscriber_id = " + user.getSubscriberID();
        	UpdateDetailsRequest req = new UpdateDetailsRequest(query);
        	ClientUI.console.accept(req);
    	}   	
    }

	@Override
	public void setResultText(Object result) {
		 Platform.runLater(() -> {
		        Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Update Details");
		        alert.setHeaderText(null);
		        alert.setContentText((String) result);
		        alert.showAndWait();
		    });	}

	@Override
	public void setUser(User user) {
		this.user = (Subscriber) user;
	}
	
	public boolean isValidEmail(String email) {
	    return email != null && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}

}
