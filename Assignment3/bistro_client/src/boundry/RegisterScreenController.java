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
    private TextArea resultTxt;
    /**
     * when the user clicks 'cancel'
     * */
    @FXML
    void onCancelClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/loginScreen.fxml",user);
    }
    /**
     * when the user clicks 'Submit' (registering the user)
     * @param event
     */
    @FXML
    void onSubmitClick(ActionEvent event) {
    	String fname = firstName.getText().trim();
    	String lname = lastName.getText().trim();
    	String phone = phoneNumber.getText().trim();
    	String userName = this.userName.getText().trim();
    	String email = this.email.getText().trim();
    	if(fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || userName.isEmpty()) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error Occurred");
    		alert.setHeaderText("Input Validation Failed");
    		alert.setContentText("please enter a value in all fields");
    		alert.showAndWait();
    	}
    	else {
    		int generatedId = this.random.nextInt(1_000_000);
    		Subscriber s = new Subscriber(generatedId,userName,fname,lname,phone,email,new ArrayList<>());
    		RegisterRequest r = new RegisterRequest(s);
    		ClientUI.console.accept(r);
    		user = s;
    	}
    }

	@Override
	public void setResultText(Object result) {
		resultTxt.setText((String)result);
	}
    
    public void setUser(User user) {
    	this.user = user;
    }
}
