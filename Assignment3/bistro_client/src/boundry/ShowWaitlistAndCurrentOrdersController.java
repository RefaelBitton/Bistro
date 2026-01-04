package boundry;

import java.io.IOException;
import java.util.List;
import entities.GetLiveStateRequest; // Import your new request
import entities.User;
import entities.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ShowWaitlistAndCurrentOrdersController implements IController {

    private User user;
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);

    @FXML private Button refreshBtn;
    @FXML private Button backBtn;
    @FXML private TextArea showWaitlist;
    @FXML private TextArea showCurrentOrders;

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
        // Automatically load data when screen opens
        loadData();
    }

    /**
     * Reusable method to send the request to the server
     */
    private void loadData() {
        // Clear current text to show user something is happening (Optional)
        showWaitlist.setText("Loading...");
        showCurrentOrders.setText("Loading...");
        
        // Send request
        GetLiveStateRequest req = new GetLiveStateRequest();
        ClientUI.console.accept(req);
    }

    @FXML
    void refreshClick(ActionEvent event) {
        loadData();
    }

    @FXML
    void onBackClick(ActionEvent event) throws IOException {
        String path = "/boundry/WorkerScreen.fxml";
        ClientUI.console.switchScreen(this, event, path, user);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        isLoggedIn.set(user != null && user.getType() != UserType.GUEST);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void setResultText(Object result) {
        // We expect a List<String> from the server now
        if (result instanceof List) {
            List<String> data = (List<String>) result;
            
            javafx.application.Platform.runLater(() -> {
                if (data.size() >= 2) {
                    showWaitlist.setText(data.get(0));      // The Waitlist String
                    showCurrentOrders.setText(data.get(1)); // The Seated String
                }
            });
        } else {
            System.out.println("Error: Expected List<String> but got " + result.getClass());
        }
    }
}