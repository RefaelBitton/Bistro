package boundry;

import java.util.Map;
import entities.GetReportsRequest;
import entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

/**
 * Controller for the Reports Screen. Displays activity and lateness charts
 * based on data retrieved from the server.
 */
public class ReportsScreenController implements IController {
    private User user;

    @FXML private BarChart<String, Number> activityChart; 
    @FXML private BarChart<String, Number> latenessChart; 
    
    /** X-Axes for both charts to set categories (hours) */
    @FXML private CategoryAxis activityXAxis;
    @FXML private CategoryAxis latenessXAxis;
    
    @FXML private Button backBtn;

	/**
	 * Initializes the controller. Sets up the charts and requests report data.
	 */
    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
        
        // 1. Force the axes to show hours 08:00 - 23:00 immediately
        setupAxes();
        
        // 2. Request data (This ensures it updates every time you enter the screen)
        ClientUI.console.accept(new GetReportsRequest());
    }

    /** Sets up the X-Axes with hour categories from 08:00 to 23:00 */
    private void setupAxes() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 8; i <= 23; i++) {
            hours.add(String.format("%02d:00", i));
        }
        // Force the X-Axis to use these categories so they always appear
        activityXAxis.setCategories(hours);
        latenessXAxis.setCategories(hours);
    }


	/**
	 * Handles the result from the server and populates the charts. Expects a Map
	 * with keys: "Arrivals", "Departures", "AvgCustomerLate", "AvgRestaurantDelay"
	 * @param result The result object from the server
	 */
    @Override
    public void setResultText(Object result) {
        if (result instanceof Map) {
            Map<String, Map<Integer, Double>> data = (Map<String, Map<Integer, Double>>) result;
            
            Platform.runLater(() -> {
                populateActivityChart(data.get("Arrivals"), data.get("Departures"));
                populateLatenessChart(data.get("AvgCustomerLate"), data.get("AvgRestaurantDelay"));
            });
        }
    }

    /** Populates the activity chart with arrivals and departures data
     * @param arrivals Map of hour to number of arrivals
     * @param departures Map of hour to number of departures
     *  */
    private void populateActivityChart(Map<Integer, Double> arrivals, Map<Integer, Double> departures) {
        activityChart.getData().clear();
        
        XYChart.Series<String, Number> seriesArr = new XYChart.Series<>();
        seriesArr.setName("Arrived");
        
        XYChart.Series<String, Number> seriesDep = new XYChart.Series<>();
        seriesDep.setName("Left");

        for (int i = 8; i <= 23; i++) { 
            String hourLabel = String.format("%02d:00", i);
            seriesArr.getData().add(new XYChart.Data<>(hourLabel, arrivals.getOrDefault(i, 0.0)));
            seriesDep.getData().add(new XYChart.Data<>(hourLabel, departures.getOrDefault(i, 0.0)));
        }
        
        activityChart.getData().addAll(seriesArr, seriesDep);
    }

	/**
	 * Populates the lateness chart with customer lateness and restaurant delay data 
	 * @param customerLate    Map of hour to average customer lateness
	 * @param restaurantDelay Map of hour to average restaurant delay
	 */
    private void populateLatenessChart(Map<Integer, Double> customerLate, Map<Integer, Double> restaurantDelay) {
		latenessChart.getData().clear();
		latenessChart.setTitle("Lateness vs Delay (Minutes)");

		XYChart.Series<String, Number> seriesCust = new XYChart.Series<>();
		seriesCust.setName("Avg Customer Late"); // Late arrival

		XYChart.Series<String, Number> seriesRest = new XYChart.Series<>();
		seriesRest.setName("Avg Restaurant Delay"); // Waiting for table

		for (int i = 8; i <= 23; i++) {
			String hourLabel = String.format("%02d:00", i);
            // Use getOrDefault to prevent NullPointer if data is missing for an hour
			seriesCust.getData().add(new XYChart.Data<>(hourLabel, customerLate.getOrDefault(i, 0.0)));
			seriesRest.getData().add(new XYChart.Data<>(hourLabel, restaurantDelay.getOrDefault(i, 0.0)));
		}

		latenessChart.getData().addAll(seriesCust, seriesRest);
	}

    /** Handles the Back button click event to return to the Worker Screen
     * @param event The action event triggered by clicking the button
     */
    @FXML
    void onBackBtnClick(ActionEvent event) {
        // This will now work because we added onAction in FXML
        ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
    }

	/**
	 * Sets the current user for the controller
	 * 
	 * @param user The user to set
	 */
    @Override
    public void setUser(User user) {
        this.user = user;
    }
}