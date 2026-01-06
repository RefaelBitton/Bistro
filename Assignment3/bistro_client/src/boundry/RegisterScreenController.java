package boundry;
import java.util.ArrayList;
import java.util.Random;

import entities.RegisterRequest;
import entities.Subscriber;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/**
 * A controller for the registration screen
 */
public class RegisterScreenController implements IController{
	/** the current user of the session*/
	private User user;
	/** for setting the subscriber id*/
	Random random;
	@FXML
	void initialize() {
		random = new Random();
		ClientUI.console.setController(this);
	}
    @FXML
    private Button cancelBtn;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField userName;
    
    @FXML
    private TextField statusField;
    
    @FXML
    private TextArea resultTxt;
    /**
     * when the user clicks 'cancel'
     * */
    @FXML
    void onCancelClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml",user);
    }
    /**
     * when the user clicks 'Submit' (registering the user)
     * @param event
     */
    @FXML
    void onSubmitClick(ActionEvent event) {
    	boolean emptyException = false;
    	boolean emailException = false;
    	boolean phoneException = false;
    	boolean statusException = false;
    	String fname = firstName.getText().trim();
    	String lname = lastName.getText().trim();
    	String phone = phoneNumber.getText().trim();
    	String userName = this.userName.getText().trim();
    	String email = this.email.getText().trim();
    	String status = this.statusField.getText().trim();
    	if(fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || userName.isEmpty() || status.isEmpty()) {
    		emptyException = true;
    	}
    	if(!isValidEmail(email)) {
    		emailException = true;
    	}
    	if(phone.length() != 10) {
    		phoneException = true;
    	}
    	if(!(status.equals("CLIENT")) && !(status.equals("EMPLOYEE"))) {
    		statusException = true;
    	}
    	if((!emailException) && (!phoneException) && (!emptyException) && (!statusException)) {
    		int generatedId = this.random.nextInt(1_000_000);
    		Subscriber s = new Subscriber(generatedId,userName,fname,lname,phone,email,status,new ArrayList<>());
    		RegisterRequest r = new RegisterRequest(s);
    		ClientUI.console.accept(r);
    		user = s;
    	}
    	if(emptyException) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error Occurred");
    		alert.setHeaderText("Input Validation Failed");
    		alert.setContentText("please enter a value in all fields");
    		alert.showAndWait();
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
    	if(statusException) {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error Occurred");
    		alert.setHeaderText("Input Validation Failed");
    		alert.setContentText("Please enter valid status: CLIENT or EMPLOYEE");
    		alert.showAndWait();
    	}
    }
    
    public boolean isValidEmail(String email) {
	    return email != null && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}

	@Override
	public void setResultText(Object result) {
		resultTxt.setText((String)result);
	}
    
    public void setUser(User user) {
    	this.user = user;
    }
}
