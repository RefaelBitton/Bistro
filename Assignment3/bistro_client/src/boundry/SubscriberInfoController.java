package boundry;

import entities.GetAllSubscribersRequest;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SubscriberInfoController implements IController {
	private User user;
	
    @FXML
    private Button backBtn;

    @FXML
    private Button displaySubscribersButton;

    @FXML
    private TextArea resultTxt;

    @FXML
    void onBackBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
    }

    @FXML
    void onDisplaySubscribersClick(ActionEvent event) {
    	ClientUI.console.accept(new GetAllSubscribersRequest());
    }

	@Override
	public void setResultText(Object result) {
		resultTxt.setText((String) result);
	}

	@Override
	public void setUser(User user) {
		this.user = user;		
	}

}
