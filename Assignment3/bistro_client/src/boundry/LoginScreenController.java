package boundry;

import java.io.IOException;

import entities.Guest;
import entities.LoginRequest;
import entities.Subscriber;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * A controller responsible for the login screen
 */
public class LoginScreenController implements IController{
	/** the string that will hold the response from the server*/
	private String serverResponse;
	/** the user using the screen*/
	private User user;
	/** setting the console's controller to this*/
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
    
    /**
     * when the user clicks 'enter as guest'
     * @param event
     * @throws IOException
     */
    @FXML
    void onGuestClick(ActionEvent event) throws IOException {
    	this.user = new Guest(null, null);
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml", user);
    }
    /**
     * when the user clicks 'login'
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
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
        	Thread.sleep(1000);
        	if(!serverResponse.equals("Not found")) {
        		System.out.println(serverResponse);
        		String[] args = serverResponse.split(",");
        		//args = [0] - full name [1] - sub_id [2] - username [3] - phone number [4] - email
        		String fname = args[0].split(" ")[0];
        		String lname = args[0].split(" ")[1];
        		user = new Subscriber(Integer.parseInt(args[1]),args[2], fname, lname, args[3],args[4], null);
        		ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml", user);
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
    		alert.showAndWait();
    	}
    	
    }
    /**
     * when the user clicks on 'Register'
     * @param event
     */
    @FXML
    void onRegisterClick(ActionEvent event) {
    	this.user = new Guest(null, null);
    	ClientUI.console.switchScreen(this, event, "/boundry/registerScreen.fxml",user);
    }
    /**
     * setting the server response
     */
	@Override
	public void setResultText(Object result) {
		serverResponse = (String)result;
		
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
