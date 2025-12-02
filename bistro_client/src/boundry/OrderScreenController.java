package boundry;

import java.io.IOException;
import java.util.ArrayList;

import entities.Order;
import entities.WriteRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class OrderScreenController {
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
    //TODO: add input checks
    //TODO: don't print when the order failed, add input check before calling console.accept()
    @FXML
    void OnOrderClick(ActionEvent event) {
    	ArrayList<String> args = new ArrayList<>();
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
    
    @FXML
    void onCancelClick(ActionEvent event) throws IOException {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml");
    	}

}
