package boundry;

import entities.GetAllActiveOrdersRequest;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class AllOrdersScreenController implements IController {
	private User user;
    @FXML
    private Button backBtn;

    @FXML
    private Button displayOrdersBtn;

    @FXML
    private TextArea resultTxt;

    @FXML
    void onBackClick(ActionEvent event) {
		ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
    }

    @FXML
    void onDisplayOrdersClick(ActionEvent event) {
    	GetAllActiveOrdersRequest req = new GetAllActiveOrdersRequest();
    	ClientUI.console.accept(req);
    }

	@Override
	public void setResultText(Object result) {
		resultTxt.setText((String)result);
	}

	@Override
	public void setUser(User user) {
		this.user = user;
		
	}

}
