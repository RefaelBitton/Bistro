package boundry;

import entities.JoinWaitlistRequest;
import entities.LeaveWaitlistRequest;
import entities.Order;
import entities.User;
import entities.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TerminalScreenController implements IController {
    private User user;
    
    @FXML private Button haveOrderBtn;
    @FXML private Button dontHaveOrderBtn;
    @FXML private Button logOutBtn;
  
    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
    }

    @Override
    public void setResultText(Object result) {
        return;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    @FXML
    void onHaveOrderClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/terminalOrderManagementScreen.fxml",user);
    }
    
    @FXML
    void onDontHaveOrderClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/ImmidiateArrival.fxml",user);
    }
    
    @FXML
    void OnLogoutClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/loginScreen.fxml",null);
    }    
}