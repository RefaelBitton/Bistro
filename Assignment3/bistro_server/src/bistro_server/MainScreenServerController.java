package bistro_server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;


/**
 * A controller for the server UI
 */
public class MainScreenServerController {
    @FXML
    private Button exitBtn;

    @FXML
    private Button showIP;

    @FXML
    private TextArea resultTxt;

    private static MainScreenServerController instance;

    @FXML
    void initialize() {
        instance = this;
        refreshClientsText();
    }
    public void updateTxt(String msg) {
		if (instance == null) return;
		Platform.runLater(() -> {
			String currentText = instance.resultTxt.getText();
			instance.resultTxt.setText(currentText + "\n" + msg);
		});
	}
    public static void refreshClientsLive() {
        if (instance == null) return;
        Platform.runLater(() -> instance.refreshClientsText());
    }

    private void refreshClientsText() {
        if (BistroServer.clients == null || BistroServer.clients.isEmpty()) {
            resultTxt.setText("No Client Connected.");
            return;
        }

        StringBuilder clientString = new StringBuilder("Clients Connected: \n");
        for (int i = 0; i < BistroServer.clients.size(); i++) {
            ConnectionToClient client = BistroServer.clients.get(i);
            if (client != null) {
                clientString.append(i + 1).append(". ").append(client.toString()).append("\n");
            }
        }
        resultTxt.setText(clientString.toString());
    }

    @FXML
    void onExitClick(ActionEvent event) {
        System.exit(0);
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
