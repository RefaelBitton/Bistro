package boundry;

import javafx.event.ActionEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainScreenController implements Initializable, IController {
	private User user;
	
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
    
    @FXML
    void onOrderClick(ActionEvent event) throws Exception {   // Method called when Order button is clicked
    	ClientUI.console.switchScreen(this, event, "/boundry/OrderScreen.fxml", user);

    }

    @FXML
    void onExitClick(ActionEvent event) {
    	System.exit(0);
    }
    
    public void setUser(User user) {
    	this.user = user;
    }

	@Override
	public void setResultText(String result) {
		return;
	}
}
