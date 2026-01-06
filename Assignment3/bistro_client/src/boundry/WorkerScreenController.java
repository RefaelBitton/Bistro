package boundry;

import entities.Guest;
import entities.User;
import entities.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WorkerScreenController implements IController {
	private User user;
	
    @FXML
    private Button bistroManagementBtn;

    @FXML
    private Button changeDetailsBtn;

    @FXML
    private Button historyBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Button orderBtn;

    @FXML
    private Button orderDetailsBtn;

    @FXML
    private Button reportBtn;

    @FXML
    private Button subscriberDetailsBtn;

    @FXML
    private Button waitingListBtn;
    
    @FXML
    private Button registerBtn;
    
    private BooleanProperty isManager = new SimpleBooleanProperty(false);
    @FXML
	void initialize() {
    	ClientUI.console.setController(this);
    	reportBtn.visibleProperty().bind(isManager);
		reportBtn.managedProperty().bind(isManager);
    }

    @FXML
    void onBistroManagementClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/BistroManagementScreen.fxml", user);
    }

    @FXML
    void onChangeDetailsClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/ChangeDetailsScreen.fxml", user);
    }

    @FXML
    void onHistoryBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/HistoryScreen.fxml", user);
    }

    @FXML
    void onLogOutClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/loginScreen.fxml", null);
    }

    @FXML
    void onNewOrderBtn(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/OrderScreen.fxml", user);
    }

    @FXML
    void onOrderDetailsClick(ActionEvent event) {
    	if(user.getType() == UserType.BISTRO_REP || user.getType() == UserType.MANAGER)
    		ClientUI.console.switchScreen(this, event, "/boundry/AllOrdersScreen.fxml", user);
    }

    @FXML
    void onReportClick(ActionEvent event) {
    	if(user.getType() == UserType.MANAGER)
    		ClientUI.console.switchScreen(this, event, "/boundry/ReportsScreen.fxml", user);
    }

    @FXML
    void onSubscriberDetailsClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/SubscriberInfoScreen.fxml", user);
    }

    @FXML
    void onWaitingListClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/CurrentStateScreen.fxml", user);
    }
    
    /**
     * when the user clicks on 'Register'
     * @param event
     */
    @FXML
    void onRegisterClick(ActionEvent event) {
    	this.user = new Guest(null, null);
    	ClientUI.console.switchScreen(this, event, "/boundry/registerScreen.fxml",user);
    }

	@Override
	public void setResultText(Object result) {
		return;
		
	}

	@Override
	public void setUser(User user) {
		this.user = user;
		isManager.set(user.getType()==UserType.MANAGER);
	}

}
