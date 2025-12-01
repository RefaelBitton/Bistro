package boundry;

import java.util.ArrayList;

import com.sun.net.httpserver.Request;

import bistro_client.Console;
import entities.Order;
import entities.WriteRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class OrderScreenController {
	private Console console = new Console("localhost",5556);
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
    void OnOrderClick(ActionEvent event) {
    	ArrayList<String> args = new ArrayList<>();
    	args.add(orderNumTxt.getText().trim());
    	args.add(OrderDateTxt.getText().trim());
    	args.add(NumberOfGuestsTxt.getText().trim());
    	args.add(ConfirmationCodeTxt.getText().trim());
    	args.add(SubscriberIdTxt.getText().trim());
    	args.add(dateOfPlacingOrderTxt.getText().trim());
    	WriteRequest r = new WriteRequest(new Order(args));
    	console.accept(r);
    }

}
