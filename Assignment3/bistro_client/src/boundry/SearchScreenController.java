package boundry;

import entities.ReadRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SearchScreenController implements IController {
	
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
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
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
    		setResultText("Please enter a valid order number: a positive integer");
        	}
    	} catch (NumberFormatException e) {
    		setResultText("Please enter a valid order number");
    	}
    }
    public void setResultText(String str) {
    	resultsTxt.setText(str);
    }

}
