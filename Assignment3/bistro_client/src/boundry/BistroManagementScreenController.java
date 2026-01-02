package boundry;

import java.time.LocalDate;
import java.util.ArrayList;

import entities.User;
import entities.WriteHoursDateRequest;
import entities.ChangeHoursDayRequest;
import entities.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class BistroManagementScreenController implements IController{
	private User user;
	@FXML private DatePicker datePicker;
	@FXML private ComboBox<Integer> dayOfWeek;
	@FXML private ComboBox<Integer> openHour;
	@FXML private ComboBox<Integer> closeHour;
	@FXML private Button confirm;
	@FXML private Button tablesBtn;
	@FXML private Button backBtn;
	@FXML private TextField AddTableCapText;
	@FXML private ComboBox<Table> currentTables;
	@FXML private CheckBox removeTableCheck;
    @FXML private TextField setTableCapText;

	@FXML
	public void initialize() {
		ClientUI.console.setController(this);
		for (int i = 1; i <= 7; i++) dayOfWeek.getItems().add(i);
		for (int i = 0; i <= 23; i++) openHour.getItems().add(i);
		for (int i = 0; i <= 23; i++) closeHour.getItems().add(i);
		LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusMonths(1);

        datePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(today) || date.isAfter(maxDate)) setDisable(true);
            }
        });
	}
	
	@FXML
	void onConfirmClick(ActionEvent event) {
		ArrayList<String> args = new ArrayList<>();
		Integer day;
		Integer open;
		Integer close;
    	try {
        	LocalDate date = datePicker.getValue();
    		day = dayOfWeek.getValue();
    		open = openHour.getValue();
    		close = closeHour.getValue();
    		
    		if((date != null && day != null) || (date == null && day == null)) {
    			Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Error Occurred");
        		alert.setHeaderText("Input Validation Failed");
        		alert.setContentText("Please choose one from date and day of week");
        		alert.showAndWait();
    		}
    		
    		else if (date != null && day == null){
    			args.add(date.toString());
    			args.add(open.toString());
    			args.add(close.toString());
    			WriteHoursDateRequest r = new WriteHoursDateRequest(args.get(0),args.get(1),args.get(2));
    			ClientUI.console.accept(r);
    		}
    		
    		else if (date == null && day != null) {
    			args.add(day.toString());
    			args.add(open.toString());
    			args.add(close.toString());
    			ChangeHoursDayRequest r = new ChangeHoursDayRequest(args.get(0),args.get(1),args.get(2));
    			ClientUI.console.accept(r);
    		}
    			   			
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	
	}
	
	@FXML
	void onTablesBtnClick(ActionEvent event) {
	
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
