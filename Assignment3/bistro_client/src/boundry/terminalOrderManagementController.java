package boundry;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class terminalOrderManagementController implements IController{
	 private User user;
	 
	 @FXML private TextField confCode;
	 @FXML private TextField email;
	 @FXML private Button lostMyCodeBtn;
	 @FXML private Button leaveTableBtn;
	 @FXML private Button getTableBtn;
	 @FXML private Button backBtn;

    @Override
    public void setResultText(Object result) {
        return;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    @FXML
    void OnBackClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/TerminalScreen.fxml",null);
    }   
}
