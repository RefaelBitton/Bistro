package boundry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entities.JoinWaitlistRequest;
import entities.Order;
import entities.User;
import entities.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class waitListController implements IController{
	private User user;
    private Integer pendingGuests;
    private String pendingSubscriberIdStr;
    private String pendingContact;
    private String pendingOrderDateTime;
	
	private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    @FXML 
    private TextField guestsNumberTxt;
    
    @FXML 
    private TextField identifyText;
    
    @FXML
    private Label idLabel;
    
    @FXML 
    private TextArea resultTxt;
    
	@FXML
	private Button backBtn;
	
	@FXML
    public void initialize() {
        ClientUI.console.setController(this);
        
        identifyText.visibleProperty().bind(isLoggedIn.not());
        idLabel.visibleProperty().bind(isLoggedIn.not());
    }
	
	/** * Requirement: Enter waitlist (Join Waitlist)
     * Collects guests and identification.
     */
    @FXML
    public void onJoinWaitlistClick(ActionEvent event) {
        String guests = guestsNumberTxt.getText().trim();
        if (guests.isEmpty() || Integer.parseInt(guests) <= 0) {
        	setResultText("Please enter a valid number of guests.");
            return;
        }
        
        else if (!OrderScreenController.isValidPhoneOrEmail(guestsNumberTxt.getText().trim())) {
			setResultText("Please enter a valid contact (phone or email).");
			return;
		}

        // Build the waitlist order entity
        ArrayList<String> args = new ArrayList<>();
        
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
    
    @Override
    public void setResultText(Object result) {
    	resultTxt.setText((String)result);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
    }
    
    @FXML
    void OnBackBtnClick(ActionEvent event) {
        ClientUI.console.switchScreen(this, event, "/boundry/TerminalScreen.fxml", user);
    }
    
    
}
