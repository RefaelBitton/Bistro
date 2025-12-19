package boundry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import entities.UpdateRequest;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UpdateScreenController implements IController {
	
	private User user;
	
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
	            .withResolverStyle(ResolverStyle.STRICT); //forces real dates
		ClientUI.console.setController(this);
	}
	
	@FXML
    void OnUpdateNumberOfGuestsClick(ActionEvent event) { 
    	ArrayList<String> args = new ArrayList<>();
    	boolean exceptionRaised = false; //to check valid entries
    	int orderNum = 0;
    	int guestsNum = 0;
    	//Input checks
    	try {
    		orderNum = Integer.parseInt(orderNumberTxt.getText().trim());
    		guestsNum = Integer.parseInt(guestsNumberTxt.getText().trim());
    		if(orderNum<=0 || guestsNum<=0) exceptionRaised = true;
    	} catch (NumberFormatException e) {
    		exceptionRaised = true;
    		orderNumberTxt.clear();
    		guestsNumberTxt.clear();
    	}
    	if(exceptionRaised) {
    		setResultText("Please enter valid entries in the fields\nFor date: in the format dd/mm/yyyy\nFor order and guests: a positive integer");
    	}
    	else {
    		args.add(orderNumberTxt.getText().trim());
        	args.add(guestsNumberTxt.getText().trim());
        	UpdateRequest r = new UpdateRequest(args.get(0), Integer.parseInt(args.get(1))); //creating request with the new number of guests
        	ClientUI.console.accept(r); //sending to client 
    	}
    	
    }
   
    @FXML
    void OnUpdateDateClick(ActionEvent event) {
    	ArrayList<String> args = new ArrayList<>();
    	boolean exceptionRaised = false; //to check valid entries
    	int orderNum = 0;
    	//Input checks
    	try {
    		orderNum = Integer.parseInt(orderNumberTxt.getText().trim());
    		if(orderNum<=0) exceptionRaised = true;
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
    		UpdateRequest r = new UpdateRequest(args.get(0), args.get(1)); //creating request with the new date
        	ClientUI.console.accept(r); //sending to client 
    	}
    	else {
    		setResultText("Please enter valid entries in the fields\nFor date: in the format dd/mm/yyyy\nFor order and guests: a positive integer");
    	}
    }	
    
    @FXML
    void OnBackBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
    }
    
    public void setResultText(String result) {
    	resultTxt.setText(result);
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
}
