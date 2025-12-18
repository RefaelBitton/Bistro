package boundry;

import entities.LoginRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginScreenController implements IController{
	private boolean flag;
	@FXML
	public void initialize() {
		ClientUI.console.setController(this);
	}
    @FXML
    private Button guestBtn;

    @FXML
    private Button logInBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField subscriberId;

    @FXML
    private TextField userName;

    @FXML
    void onGuestClick(ActionEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundry/mainScreen.fxml"));
		MainScreenController main = loader.getController();
		main.setLoggedIn(false);
		ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
    }

    @FXML
    void onLoginClick(ActionEvent event) {
    	int id;
    	boolean exceptionRaised = false;
    	try {
    		id = Integer.parseInt(subscriberId.getText().trim());
    		exceptionRaised = id<=0;
    	} catch (NumberFormatException e) {
    		exceptionRaised = true;
    	}
    	if (!exceptionRaised) {
        	LoginRequest r = new LoginRequest(id);
        	ClientUI.console.accept(r);
        	if(flag) {
        		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundry/mainScreen.fxml"));
        		MainScreenController main = loader.getController();
        		main.setLoggedIn(true);
        		ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
        	}
        	else{
        		Alert alert = new Alert(AlertType.ERROR);
        	    alert.setTitle("Error Occurred");
        	    alert.setHeaderText("Input Validation Failed");
        	    alert.setContentText("That user doesn't exist, please check your credentials");

        	    alert.showAndWait();
        	}    		
    	}
    	
    	else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error Occurred");
    		alert.setHeaderText("Input Validation Failed");
    		alert.setContentText("an id must be a positive integer");
    	}
    	
    }

    @FXML
    void onRegisterClick(ActionEvent event) {

    }

	@Override
	public void setResultText(String result) {
		flag = result.equals("User found");
		
	}

}
