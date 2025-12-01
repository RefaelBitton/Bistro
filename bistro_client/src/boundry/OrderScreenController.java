package boundry;

import java.io.IOException;
import java.util.ArrayList;

import com.sun.net.httpserver.Request;

import bistro_client.Console;
import entities.Order;
import entities.WriteRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    	FXMLLoader loader = new FXMLLoader();
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	Stage primaryStage = new Stage();
    	Pane root = loader.load(getClass().getResource("/boundry/mainScreen.fxml").openStream());
    	Scene scene = new Scene(root);
    	primaryStage.setScene(scene);		
    	primaryStage.show();
    	}

}
