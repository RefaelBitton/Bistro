package boundry;

import java.util.ArrayList;

import entities.Order;
import entities.ReadRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    	FXMLLoader loader = new FXMLLoader();
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	Stage primaryStage = new Stage();
    	Pane root = loader.load(getClass().getResource("/boundry/mainScreen.fxml").openStream());
    	Scene scene = new Scene(root);
    	primaryStage.setScene(scene);		
    	primaryStage.show();

    }
    @FXML
    void onSearchClick(ActionEvent event) {
    	ArrayList<String> argsForOrder = new ArrayList<>();
    	argsForOrder.add(orderNumTxt.getText().trim());
    	argsForOrder.add("");
    	argsForOrder.add("");
    	argsForOrder.add("");
    	argsForOrder.add("");
    	argsForOrder.add("");
    	ReadRequest r = new ReadRequest(new Order(argsForOrder));
    	ClientUI.console.accept(r);
    	
    	
    }
    public void setResultTxt(String str) {
    	resultsTxt.setText(str);
    }

}
