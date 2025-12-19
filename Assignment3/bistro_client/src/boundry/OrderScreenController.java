package boundry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import entities.Order;
import entities.User;
import entities.WriteRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class OrderScreenController implements IController {
	
	private User user;
	
    DateTimeFormatter formatter;
	@FXML
	public void initialize() {
		formatter =DateTimeFormatter
	            .ofPattern("dd/MM/uuuu")
	            .withResolverStyle(ResolverStyle.STRICT); // forces real dates
		ClientUI.console.setController(this);
	}
	
	@FXML
    private TextField ConfirmationCodeTxt;

    @FXML
    private TextField NumberOfGuestsTxt;

    @FXML
    private Button OrderBtn;

    @FXML
    private TextField OrderDateTxt;

    @FXML
    private TextField SubscriberIdTxt;

    @FXML
    private TextField dateOfPlacingOrderTxt;

    @FXML
    private TextField orderNumTxt;
    
    @FXML
    private TextArea resultTxt;

    @FXML
    private Button cancelBtn;
    
    @FXML
    void OnOrderClick(ActionEvent event) {
    	ArrayList<String> args = new ArrayList<>();
    	boolean exceptionRaised = false;
    	int orderNum = 0;
    	int numberOfGuests = 0;
    	int confirmationCode = 0;
    	int subscriberId = 0;
    	//Input checks
    	try {
    		//parsing integers fields
    		orderNum = Integer.parseInt(orderNumTxt.getText().trim());
    		numberOfGuests = Integer.parseInt(NumberOfGuestsTxt.getText().trim());
    		confirmationCode = Integer.parseInt(ConfirmationCodeTxt.getText().trim());
    		subscriberId = Integer.parseInt(SubscriberIdTxt.getText().trim());
    		if(orderNum<=0 || numberOfGuests <=0 || confirmationCode <=0 || subscriberId <=0) {
    			exceptionRaised = true;
    		}
    	} catch (NumberFormatException e) { //wrong input handling
    		exceptionRaised = true;
    		orderNumTxt.clear();
    		NumberOfGuestsTxt.clear();
    		ConfirmationCodeTxt.clear();
    		SubscriberIdTxt.clear();
    	}
    	//parsing date fields
    	try {
    		LocalDate.parse(OrderDateTxt.getText().trim(),formatter);
    		LocalDate.parse(dateOfPlacingOrderTxt.getText().trim(),formatter);
    	} catch (DateTimeParseException e) { //wrong input handling
    		exceptionRaised = true;
    		OrderDateTxt.clear();
    		dateOfPlacingOrderTxt.clear();
    	}
    	if(exceptionRaised) {
    		setResultText("Please enter valid entries in the fields\nFor date: in the format dd/mm/yyyy\nFor anything else: a positive integer");
    	}
    	else {
    		args.add(orderNumTxt.getText().trim());
    		args.add(OrderDateTxt.getText().trim());
        	args.add(NumberOfGuestsTxt.getText().trim());
        	args.add(ConfirmationCodeTxt.getText().trim());
        	args.add(SubscriberIdTxt.getText().trim());
        	args.add(dateOfPlacingOrderTxt.getText().trim());
        	WriteRequest r = new WriteRequest(new Order(args)); //creating request with the new order
        	ClientUI.console.accept(r); //sending request to client
        	resultTxt.setText(r.getOrder().toString()); //printing the new order details on the screen
    	}
    }
    
    @FXML
    void onCancelClick(ActionEvent event) throws IOException {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
    }
    
    public void setResultText(String result) {
    	resultTxt.setText(result);
    }

    public void setUser(User user) {
    	this.user = user;
    }    
}
