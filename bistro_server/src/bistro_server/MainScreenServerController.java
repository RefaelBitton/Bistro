package bistro_server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class MainScreenServerController {
    @FXML
    private Button exitBtn;
    
    @FXML
    private Button showIP;
    
    @FXML
    private TextArea resultTxt;
    
    @FXML
    void onExitClick(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void onShowIpClick(ActionEvent event) {
    	if (BistroServer.clients.size() == 0) {
    		 resultTxt.setText("No Client Connected.");
    		 return;
    	}
    	
    	String clientString = "Clients Connected: \n";
    	
    	for (int i = 0; i < BistroServer.clients.size(); i++) {
    		String client = BistroServer.clients.get(i).toString();
    		if(client!="null") {
			clientString += i+1 +". " + BistroServer.clients.get(i).toString() + "\n";
    		}
		}
    	resultTxt.setText(clientString);
    }
    
    public void start(Stage primaryStage) throws Exception {  // Method for starting the main screen
        // Load the main screen FXML into a Parent node
        Parent root = FXMLLoader.load(getClass().getResource("/bistro_server/serverui.fxml"));

        Scene scene = new Scene(root);                        // Create the scene with the loaded layout
        primaryStage.setTitle("Server UI"); // Set the window title
        primaryStage.setScene(scene);                         // Set the scene on the primary stage
        primaryStage.show();                                  // Display the window
    }
    
}
