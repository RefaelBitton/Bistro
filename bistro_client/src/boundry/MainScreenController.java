package boundry;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainScreenController {
	
    @FXML
    private Button OrderBtn;

    @FXML
    private Button viewBtn;

    @FXML
    void onOrderClick(ActionEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root;
			root = loader.load(getClass().getResource("/boundry/OrderScreen.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);		
			primaryStage.show();
		//StudentFormController studentFormController = loader.getController();		
		//studentFormController.loadStudent(ChatClient.s1);
	
					

	
    }

    @FXML
    void onViewClick(ActionEvent event) {

    }
    
    public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/boundry/mainScreen.fxml"));
				
		Scene scene = new Scene(root);
		primaryStage.setTitle("Bistro Order management tool");
		primaryStage.setScene(scene);
		
		primaryStage.show();	 	   
	}
}
