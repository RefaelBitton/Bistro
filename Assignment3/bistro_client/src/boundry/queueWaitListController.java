package boundry;

import java.io.IOException;
import java.util.Optional;

import entities.LeaveWaitlistRequest;
import entities.User;

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
	@FXML private TextField orderNumTxt;
	@FXML private TextField confCodeTxt;
	@FXML private TextArea resultTxt;
	@FXML private Button spotBtn;
	@FXML private Button leaveWaitListBtn;
	@FXML private Button backBtn;
	
	
	@FXML
	void initialize() {
		ClientUI.console.setController(this);
	}
	
	@FXML
	void onSpotBtnClick(ActionEvent event) {
		setResultText("1");
	}
	
	@FXML
	void onLeaveBtnClick(ActionEvent event) throws IOException, InterruptedException{
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
        	alert.setHeaderText("You will be removed from waiting list");
        	alert.setContentText("Are you sure you want to continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.isPresent() && result.get() == ButtonType.OK) {
        		LeaveWaitlistRequest r = new LeaveWaitlistRequest(orderNumTxt.getText().trim());
            	ClientUI.console.accept(r);
        	}
    	}
	}
	
    @FXML
    void onBackBtnClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/ClientScreen.fxml", user);

    }
	
	@Override
    public void setUser(User user) {
        this.user = user;
    }
	
	@Override
    public void setResultText(Object result) {
		String message = (String) result;
		resultTxt.setText(message);
	}

	
}
