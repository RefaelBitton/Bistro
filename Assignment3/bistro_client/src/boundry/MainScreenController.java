package boundry;

import javafx.event.ActionEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import entities.User;
import entities.UserType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
/**
 * The controller for the main screen of the app, after logging in or entering as a guest
 * */
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
    private Button terminalBtn;
    
    @FXML
    private Button changePersonalDetailsBtn;
    /** binds some of the buttons to be available (visible) only to users who are subscribed*/
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    /** binds some of the buttons to be visible to subscribers only  */
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
    /**
     * Method called when Order button is clicked
     * @param event
     * @throws Exception
     */
    @FXML
    void onOrderClick(ActionEvent event) throws Exception {
    	ClientUI.console.switchScreen(this, event, "/boundry/OrderScreen.fxml",user);
    }
    
    @FXML
    void onCancelOrderClick(ActionEvent event) throws Exception {
    	ClientUI.console.switchScreen(this, event, "/boundry/CancelScreen.fxml",user);
    }

    @FXML
    void onExitClick(ActionEvent event) {
    	System.exit(0);
    }
    
    public void setUser(User user) {
    	this.user = user;
    	isLoggedIn.setValue(user.getType()==UserType.SUBSCRIBER);
    }

	@Override
	public void setResultText(String result) {
		return;
	}
	
	/**
	 * Handled when the user clicks "Enter Terminal".
	 * Switches the view to the Terminal GUI.
	 */
	@FXML
	void onTerminalClick(ActionEvent event) throws Exception {
	    // Navigate to the terminal screen while passing the current user context
	    ClientUI.console.switchScreen(this, event, "/boundry/TerminalScreen.fxml", user);
	}
}
