package boundry;
import java.util.ArrayList;
import java.util.Random;

import entities.RegisterRequest;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegisterScreenController implements IController{
	
	Random random;
	@SuppressWarnings("unused")
	@FXML
	void initialize() {
		Random random = new Random();
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
    private TextArea resultText;

    @FXML
    void onCancelClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/loginScreen.fxml");
    }

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
    	}
    	else {
    		int generatedId = this.random.nextInt(1_000_000);
    		RegisterRequest r = new RegisterRequest(new User(generatedId,userName,fname,lname,phone,email,new ArrayList<>()));
    		ClientUI.console.accept(r);
    	}
    	
    }

	@Override
	public void setResultText(String result) {
		resultText.setText(result);
	}
    

}
