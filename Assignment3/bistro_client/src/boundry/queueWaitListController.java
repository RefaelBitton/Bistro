package boundry;

import java.io.IOException;
import java.util.Optional;

import entities.CancelRequest;
import entities.AlterWaitlistRequest;
import entities.Request;
import entities.RequestType;
import entities.User;
import entities.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class queueWaitListController implements IController{
	private User user;
	@FXML private TextField confCodeTxt;
	@FXML private TextArea resultTxt;
	@FXML private Button spotBtn;
	@FXML private Button leaveWaitListBtn;
	@FXML private Button backBtn;
	
	//need to be done when we will have the logic of searching in waiting list
	
	 @FXML
	    void initialize() {
	    	ClientUI.console.setController(this);
	    }
	 
	@FXML
	void onSpotBtnClick(ActionEvent event) {
		boolean exceptionRaised = false;
    	int code = 0;
    	try {
    		//parsing integers fields
    		code = Integer.parseInt(confCodeTxt.getText().trim());
    		if(code <=0) {
    			exceptionRaised = true;
    		}
    	}catch (NumberFormatException e) { 
			exceptionRaised = true;
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
        	AlterWaitlistRequest r = new AlterWaitlistRequest(confCodeTxt.getText().trim(),RequestType.SPOT_WAITLIST);
            ClientUI.console.accept(r);
        	
    	}
	}
	
	@FXML
	void onLeaveBtnClick(ActionEvent event) throws IOException, InterruptedException{
		boolean exceptionRaised = false;
    	int code = 0;
    	try {
    		//parsing integers fields
    		code = Integer.parseInt(confCodeTxt.getText().trim());
    		if(code <=0) {
    			exceptionRaised = true;
    		}
    	}catch (NumberFormatException e) { 
			exceptionRaised = true;
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
        	AlterWaitlistRequest r = new AlterWaitlistRequest(confCodeTxt.getText().trim(),RequestType.LEAVE_WAITLIST);
            ClientUI.console.accept(r);
        	
    	}
	}
	
    @FXML
    void onBackBtnClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/ClientScreen.fxml", null);

    }
	
	@Override
    public void setUser(User user) {
        this.user = user;
    }
	
	@Override
    public void setResultText(Object result) {
		String message = (String) result;
		System.out.println("Leave waitlist response received: " + message);
		resultTxt.setText(message);
	}
}
