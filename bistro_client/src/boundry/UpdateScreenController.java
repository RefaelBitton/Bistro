package boundry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import entities.UpdateRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UpdateScreenController {
	@FXML
	private TextField orderNumberTxt;
	
	@FXML
	private TextField guestsNumberTxt;
	
	@FXML
	private TextField dateTxt;
	
	@FXML
	private TextArea resultTxt;
	
	@FXML
	private Button updateNumberBtn;
	
	@FXML
	private Button updateDateBtn;
	
	@FXML
	private Button backBtn;
	
	DateTimeFormatter formatter;
	
	@FXML
	public void initialize() {
		formatter =DateTimeFormatter
	            .ofPattern("dd/MM/uuuu")
	            .withResolverStyle(ResolverStyle.STRICT); // forces real dates
		ClientUI.console.setUpdateController(this);
	}
	
	@FXML
    void OnUpdateNumberOfGuestsClick(ActionEvent event) {
    	ArrayList<String> args = new ArrayList<>();
    	boolean exceptionRaised = false;
    	//Input checks
    	try {
    		Integer.parseInt(orderNumberTxt.getText().trim());
    		Integer.parseInt(guestsNumberTxt.getText().trim());
    	} catch (NumberFormatException e) {
    		exceptionRaised = true;
    		orderNumberTxt.clear();
    		guestsNumberTxt.clear();
    	}
    	if(!exceptionRaised) {
    		args.add(orderNumberTxt.getText().trim());
        	args.add(guestsNumberTxt.getText().trim());
        	UpdateRequest r = new UpdateRequest(args.get(0), Integer.parseInt(args.get(1)));
        	ClientUI.console.accept(r);
    	}
    }
   
    @FXML
    void OnUpdateDateClick(ActionEvent event) {
    	ArrayList<String> args = new ArrayList<>();
    	boolean exceptionRaised = false;
    	//Input checks
    	try {
    		Integer.parseInt(orderNumberTxt.getText().trim());
    	} catch (NumberFormatException e) {
    		exceptionRaised = true;
    		orderNumberTxt.clear();
    	}
    	try {
    		LocalDate.parse(dateTxt.getText().trim(),formatter);
    	} catch (DateTimeParseException e) {
    		exceptionRaised = true;
    		dateTxt.clear();
    	}
    	if(!exceptionRaised) {
    		args.add(orderNumberTxt.getText().trim());
    		args.add(dateTxt.getText().trim());
    		UpdateRequest r = new UpdateRequest(args.get(0), args.get(1));
        	ClientUI.console.accept(r);
    	}
    }	
    
    @FXML
    void OnBackBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
    }
    
    public void setResultText(String result) {
    	resultTxt.setText(result);
    }
}
