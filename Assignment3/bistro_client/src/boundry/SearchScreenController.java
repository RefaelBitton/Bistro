package boundry;

import entities.ReadRequest;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class SearchScreenController implements IController {
	
	private User user;
	
	@FXML
	public void initialize() {
	    ClientUI.console.setController(this);
	}
	
    @FXML
    private Button cancelBtn;


    @FXML
    private TextField orderNumTxt;

    @FXML
    private TextArea resultsTxt;

    @FXML
    private Button searchBtn;
    
    @FXML
    void onCancelClick(ActionEvent event) throws Exception {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml",user);
    }
    @FXML
    void onSearchClick(ActionEvent event) {
    	int orderNum = 0;
    	//input checks
    	try {
    		orderNum = Integer.parseInt(orderNumTxt.getText().trim());
    		ReadRequest r = new ReadRequest(orderNumTxt.getText().trim()); //creating request to search by order number because its primary key
        	if(orderNum > 0) {
        		ClientUI.console.accept(r);
        	}
        	else {
    		Alert alert = new Alert(AlertType.ERROR);
    	    alert.setTitle("Error Occurred");
    	    alert.setHeaderText("Input Validation Failed");
    	    alert.setContentText("order number must be positive number");
    	    alert.showAndWait();
        	}
    	} catch (NumberFormatException e) {
    		Alert alert = new Alert(AlertType.ERROR);
    	    alert.setTitle("Error Occurred");
    	    alert.setHeaderText("Input Validation Failed");
    	    alert.setContentText("order number must be positive number");
    	    alert.showAndWait();
    	}
    }
    public void setResultText(String str) {
    	resultsTxt.setText(str);
    }

    public void setUser(User user) {
    	this.user = user;
    }
}
