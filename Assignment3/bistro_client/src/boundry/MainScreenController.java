package boundry;

import javafx.event.ActionEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainScreenController implements Initializable {
	
    @FXML
    private Button OrderBtn;  

    @FXML
    private Button cancelOrderBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    private Button viewHistoryBtn;
    
    @FXML
    private Button waitBtn;
    
    @FXML
    private Button changePersonalDetailsBtn;
    
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind protected buttons - invisible/managed=false for guests
        viewHistoryBtn.visibleProperty().bind(isLoggedIn);
        viewHistoryBtn.managedProperty().bind(isLoggedIn);
        changePersonalDetailsBtn.visibleProperty().bind(isLoggedIn);
        changePersonalDetailsBtn.managedProperty().bind(isLoggedIn);
        
        // Optionally disable others too
        // cancelOrderBtn.visibleProperty().bind(isLoggedIn);
    }
    
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn.set(loggedIn);
    }
    
    @FXML
    void onOrderClick(ActionEvent event) throws Exception {   // Method called when Order button is clicked
    	ClientUI.console.switchScreen(this, event, "/boundry/OrderScreen.fxml");
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
    

}
