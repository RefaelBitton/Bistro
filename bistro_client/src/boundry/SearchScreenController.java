package boundry;

import entities.ReadRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SearchScreenController {
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
    	ReadRequest r = new ReadRequest(orderNumTxt.getText().trim());
    	ClientUI.console.accept(r);
    }
    public void setResultTxt(String str) {
    	resultsTxt.setText(str);
    }

}
