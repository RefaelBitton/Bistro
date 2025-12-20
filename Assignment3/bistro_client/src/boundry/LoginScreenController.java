package boundry;

import java.io.IOException;

import entities.Guest;
import entities.LoginRequest;
import entities.Subscriber;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController implements IController{
	private boolean flag;
	private User user;
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
    private Button exitBtn;
    
    @FXML
    void onGuestClick(ActionEvent event) throws IOException {
    	this.user = new Guest(null, null);
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml", user);
    }

    @FXML
    void onLoginClick(ActionEvent event) throws IOException, InterruptedException {
    	int id = 0;
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
        	Thread.sleep(100);
        	if(flag) {
        		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundry/mainScreen.fxml"));
        		Parent root = loader.load();
        		MainScreenController main = loader.getController();
        		user = new Subscriber(1,null, null, null, null, null, null);
        		((IController)main).setUser(user);
        		Scene scene = new Scene(root);
        		Stage primaryStage = new Stage();
        		((Node)event.getSource()).getScene().getWindow().hide();    // Hide primary window (current window)
        		primaryStage.setScene(scene);
        		primaryStage.show();
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
    	this.user = new Guest(null, null);
    	ClientUI.console.switchScreen(this, event, "/boundry/registerScreen.fxml",user);
    }

	@Override
	public void setResultText(String result) {
		flag = result.equals("User found");
		
	}
    @FXML
    void onExitClick(ActionEvent event) {
    	System.exit(0);
    }
	
    public void start(Stage primaryStage) throws Exception {  // Method for starting the main screen
        // Load the main screen FXML into a Parent node
        Parent root = FXMLLoader.load(getClass().getResource("/boundry/loginScreen.fxml"));

        Scene scene = new Scene(root);                        // Create the scene with the loaded layout
        primaryStage.setTitle("Bistro Order management tool"); // Set the window title
        primaryStage.setScene(scene);                         // Set the scene on the primary stage
        primaryStage.show();                                  // Display the window
    }

    public void setUser(User user) {
    	this.user = user;
    }
}
