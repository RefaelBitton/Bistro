package boundry;

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
    void onOrderClick(ActionEvent event) throws Exception {   // Method called when Order button is clicked
        FXMLLoader loader = new FXMLLoader();                 // Create a new FXMLLoader instance
        ((Node)event.getSource()).getScene().getWindow().hide(); // Hide primary window (current window)
        Stage primaryStage = new Stage();                     // Create a new stage (new window)
        Pane root;                                            // Declare the root Pane for the new scene
        
        // Load the Order screen FFXML file into the root pane
        root = loader.load(getClass().getResource("/boundry/OrderScreen.fxml").openStream());  

        Scene scene = new Scene(root);                        // Create a new scene with the loaded UI
        primaryStage.setScene(scene);                         // Set the scene to the new stage
        primaryStage.show();                                  // Show the new window
    }

    @FXML
    void onViewClick(ActionEvent event) throws Exception {    // Method called when View button is clicked
        FXMLLoader loader = new FXMLLoader();                 // Create a new FXMLLoader instance
        ((Node)event.getSource()).getScene().getWindow().hide(); // Hide primary window (current window)
        Stage primaryStage = new Stage();                     // Create a new stage (new window)
        Pane root;                                            // Declare the root Pane for the new scene
        
        // Load the Search screen FFXML file
        root = loader.load(getClass().getResource("/boundry/SearchScreen.fxml").openStream());

        Scene scene = new Scene(root);                        // Create a new scene with the loaded UI
        primaryStage.setScene(scene);                         // Set the scene to the new stage
        primaryStage.show();                                  // Show the new window
    }
    
    public void start(Stage primaryStage) throws Exception {  // Method for starting the main screen
        // Load the main screen FXML into a Parent node
        Parent root = FXMLLoader.load(getClass().getResource("/boundry/mainScreen.fxml"));

        Scene scene = new Scene(root);                        // Create the scene with the loaded layout
        primaryStage.setTitle("Bistro Order management tool"); // Set the window title
        primaryStage.setScene(scene);                         // Set the scene on the primary stage
        primaryStage.show();                                  // Display the window
    }
}
