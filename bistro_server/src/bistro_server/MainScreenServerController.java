package bistro_server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;





import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;


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
        try {
            boolean connected = false;
            String ipAddress = "No IPv4 found";
            String hostName = "Unknown";
            
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (ni.isUp() && !ni.isLoopback() && 
                    (ni.getDisplayName().contains("Wi-Fi") || 
                     ni.getDisplayName().contains("Wireless") ||
                     ni.getName().contains("wlan") ||
                     ni.getName().contains("wlp"))) {
                    
                    for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
                        if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                            ipAddress = addr.getHostAddress();
                            hostName = addr.getHostName(); // Gets hostname like "MyLaptop.local"
                            connected = true;
                            break;
                        }
                    }
                    if (connected) break;
                }
            }
            
            if (connected) {
                resultTxt.setText("WiFi Connected\n" +
                                "Hostname: " + hostName + "\n" +
                                "IPv4: " + ipAddress);
            } else {
                resultTxt.setText("No WiFi IPv4 found - Connect to WiFi");
            }
            
        } catch (SocketException e) {
            resultTxt.setText("Error checking WiFi IP");
        }
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
