package boundry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import entities.Order;
import entities.WriteRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class OrderScreenController {
    DateTimeFormatter formatter;
	@FXML
	public void initialize() {
		formatter =DateTimeFormatter
	            .ofPattern("dd/MM/uuuu")
	            .withResolverStyle(ResolverStyle.STRICT); // forces real dates
		ClientUI.console.setOrderController(this);
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
    	//Input checks
    	try {
    		Integer.parseInt(orderNumTxt.getText().trim());
    		Integer.parseInt(NumberOfGuestsTxt.getText().trim());
    		Integer.parseInt(ConfirmationCodeTxt.getText().trim());
    		Integer.parseInt(SubscriberIdTxt.getText().trim());
    	} catch (NumberFormatException e) {
    		exceptionRaised = true;
    		resultTxt.setText("Please enter valid text in the fields");
    		orderNumTxt.clear();
    		NumberOfGuestsTxt.clear();
    		ConfirmationCodeTxt.clear();
    		SubscriberIdTxt.clear();
    	}
    	try {
    		LocalDate.parse(OrderDateTxt.getText().trim(),formatter);
    		LocalDate.parse(dateOfPlacingOrderTxt.getText().trim(),formatter);
    	} catch (DateTimeParseException e) {
    		exceptionRaised = true;
    		OrderDateTxt.clear();
    		dateOfPlacingOrderTxt.clear();
    		resultTxt.setText("Please enter valid text in the fields");
    	}
    	if(!exceptionRaised) {
    		args.add(orderNumTxt.getText().trim());
    		args.add(OrderDateTxt.getText().trim());
        	args.add(NumberOfGuestsTxt.getText().trim());
        	args.add(ConfirmationCodeTxt.getText().trim());
        	args.add(SubscriberIdTxt.getText().trim());
        	args.add(dateOfPlacingOrderTxt.getText().trim());
        	WriteRequest r = new WriteRequest(new Order(args));
        	ClientUI.console.accept(r);
        	resultTxt.setText(r.getOrder().toString());
    	}
    }
    
    @FXML
    void onCancelClick(ActionEvent event) throws IOException {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
    }
    
    public void setResultText(String result) {
    	resultTxt.setText(result);
    }

}
