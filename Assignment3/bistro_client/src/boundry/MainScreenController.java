package boundry;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainScreenController {
	
    @FXML
    private Button OrderBtn;  

    @FXML
    private Button viewBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    private Button updateBtn;


    @FXML
    void onOrderClick(ActionEvent event) throws Exception {   // Method called when Order button is clicked
    	ClientUI.console.switchScreen(this, event, "/boundry/OrderScreen.fxml");
    }

    @FXML
    void onViewClick(ActionEvent event) throws Exception {    // Method called when View button is clicked
        ClientUI.console.switchScreen(this, event, "/boundry/SearchScreen.fxml");
    }
    
    public void start(Stage primaryStage) throws Exception {  // Method for starting the main screen
        // Load the main screen FXML into a Parent node
        Parent root = FXMLLoader.load(getClass().getResource("/boundry/mainScreen.fxml"));

        Scene scene = new Scene(root);                        // Create the scene with the loaded layout
        primaryStage.setTitle("Bistro Order management tool"); // Set the window title
        primaryStage.setScene(scene);                         // Set the scene on the primary stage
        primaryStage.show();                                  // Display the window
    }
    @FXML
    void onExitClick(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void onUpdateClick(ActionEvent event) throws Exception {   // Method called when update button is clicked
    	ClientUI.console.switchScreen(this, event, "/boundry/UpdateScreen.fxml");
    }
}
