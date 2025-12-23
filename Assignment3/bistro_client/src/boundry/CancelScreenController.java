package boundry;

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

public class CancelScreenController implements IController{
	private User user;
	
	@FXML
	private TextField orderNumTxt;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button backBtn;
	
	@FXML
	void onCancelClick(ActionEvent event) throws IOException, InterruptedException{
		boolean exceptionRaised = false;
    	int orderNum = 0;
    	try {
    		//parsing integers fields
    		orderNum = Integer.parseInt(orderNumTxt.getText().trim());
    		if(orderNum <= 0) {
    			exceptionRaised = true;
    		}
    	}catch (NumberFormatException e) { 
			exceptionRaised = true;
			orderNumTxt.clear();
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
        		CancelRequest c = new CancelRequest(orderNumTxt.getText().trim());
            	ClientUI.console.accept(c);
        	}
    	}
	}
	
    @FXML
    void OnBackBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml",user);
    }
    
	public void setResultText(String result) {
		return;
	}
	
    public void setUser(User user) {
    	this.user = user;
    }
}
