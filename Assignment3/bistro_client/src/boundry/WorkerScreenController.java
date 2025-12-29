package boundry;

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
    
    private BooleanProperty isManager = new SimpleBooleanProperty(false);
    @FXML
	void initialize() {
    	ClientUI.console.setController(this);
    	reportBtn.visibleProperty().bind(isManager);
		reportBtn.managedProperty().bind(isManager);
    }

    @FXML
    void onBistroManagementClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/BistroManagementScreen", user);
    }

    @FXML
    void onChangeDetailsClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/ChangeDetailsScreen", user);
    }

    @FXML
    void onHistoryBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/HistoryScreen", user);
    }

    @FXML
    void onLogOutClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/loginScreen", null);
    }

    @FXML
    void onNewOrderBtn(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/OrderScreen", user);
    }

    @FXML
    void onOrderDetailsClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/AllOrdersScreen", user);
    }

    @FXML
    void onReportClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/ReportScreen", user);

    }

    @FXML
    void onSubscriberDetailsClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/SubscriberInfoScreen", user);
    }

    @FXML
    void onWaitingListClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/CurrentStateScreen", user);
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
