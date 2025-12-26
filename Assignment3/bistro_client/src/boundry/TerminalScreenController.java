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

    @FXML 
    private TextField guestsNumTxt;
    
    @FXML 
    private TextField confCodeTxt;
    
    @FXML 
    private TextArea terminalStatus;
    
    @FXML 
    private TextField orderNumTxt;
    
	@FXML
	private Button backBtn;
    

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
    }

    /** * Requirement: Enter waitlist (Join Waitlist)
     * Collects guests and identification.
     */
    @FXML
    void onJoinWaitlistClick(ActionEvent event) {
        String guests = guestsNumTxt.getText().trim();
        if (guests.isEmpty() || Integer.parseInt(guests) <= 0) {
            terminalStatus.setText("❌ Please enter a valid number of guests.");
            return;
        }

        // Build the waitlist order entity
        ArrayList<String> args = new ArrayList<>();
        
        // Logic from your screenshot: Use timestamp for temporary ID
        String tempOrderNum = "WAIT_" + System.currentTimeMillis();
        args.add(tempOrderNum); 
        
        args.add(LocalDate.now().toString());            // Current Date
        args.add(LocalTime.now().toString());            // Current Time
        args.add(guests);
        
        // Use generated code for confirmation
        args.add("CONF_" + System.currentTimeMillis()); 
        
        // Check if user is Subscriber or Guest for ID field
        String subId = "0";
        if (user.getType() == UserType.SUBSCRIBER) {
            subId = String.valueOf(((entities.Subscriber)user).getSubscriberID());
        }
        args.add(subId);
        args.add(user.getPhone()); // Identification contact

        Order waitOrder = new Order(args);
        
        // Send concrete request
        ClientUI.console.accept(new JoinWaitlistRequest(waitOrder));
    }
    
    @FXML
    void onCancelWaitlistClick(ActionEvent event) {
        String orderNum = orderNumTxt.getText().trim();
        if(!orderNum.isEmpty()) {
            // Correctly instantiating the concrete subclass
            ClientUI.console.accept(new LeaveWaitlistRequest(orderNum));
        }
    }

    /** * Requirement: Receive Table/Identify
     * User enters confirmation code at terminal to get their table number.
     */
    @FXML
    void onIdentifyClick(ActionEvent event) {
        String code = confCodeTxt.getText().trim();
        if (code.isEmpty()) {
            terminalStatus.setText("❌ Enter your confirmation code.");
            return;
        }
        // Logic: Send request to server to check if table is ready for this code
        // RequestType.RECEIVE_TABLE (Needs to be added to your Enum)
    }

    /** * Requirement: Pay Bill / Clear Table
     * Table becomes free immediately upon payment.
     */
    @FXML
    void onPayClick(ActionEvent event) {
        // Logic: Identify table by conf code and process 10% discount for subscribers.
        // RequestType.PAY_AND_CLEAR
    }

    @Override
    public void setResultText(Object result) {
        terminalStatus.setText((String)result);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    @FXML
    void OnBackBtnClick(ActionEvent event) {
    	ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml",user);
    }


    
}