package boundry;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BistroManagementScreenController implements IController{
	private User user;
	@FXML private Button tablesBtn;
	@FXML private Button hoursBtn;
	@FXML private Button backBtn;
	
	@FXML
	void onTablesBtnClick(ActionEvent event) {
		
	}
	
	@FXML
	void onHoursBtnClick(ActionEvent event) {
		
	}
	
	@FXML
	void onBackBtnClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
	}
	
	@Override
	public void setResultText(Object result) {
		
	}
	
	@Override
	public void setUser(User user) {
		this.user = user;	
	}	
}
