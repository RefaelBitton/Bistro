package boundry;

import entities.User;
import entities.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ClientScreenController implements IController {
	private User user;
    @FXML
    private Button changeDetailsBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Button orderHistoryBtn;

    @FXML
    private Button orderManagementBtn;
    
    
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    
    @FXML
    void initialize() {
    	ClientUI.console.setController(this);
    	changeDetailsBtn.visibleProperty().bind(isLoggedIn);
    	changeDetailsBtn.managedProperty().bind(isLoggedIn);
    	orderHistoryBtn.visibleProperty().bind(isLoggedIn);
    	orderHistoryBtn.managedProperty().bind(isLoggedIn);
    }

    @FXML
    void onChangeDetailsClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/ChangeDetailsScreen.fxml", user);
    }

    @FXML
    void onLogOutBtnClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/loginScreen.fxml", null);

    }

    @FXML
    void onOrderHistoryClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/HistoryScreen.fxml", user);

    }

    @FXML
    void onOrderManagementClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/AppOrderManagementScreen.fxml", user);
    }

	@Override
	public void setResultText(Object result) {
		return;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
	}

}
