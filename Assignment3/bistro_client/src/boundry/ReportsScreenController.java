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

public class ReportsScreenController implements IController {
    private User user;

    @FXML private BarChart<String, Number> activityChart; 
    @FXML private BarChart<String, Number> latenessChart; 
    
    // NEW: Injecting the Axes to force the labels
    @FXML private CategoryAxis activityXAxis;
    @FXML private CategoryAxis latenessXAxis;
    
    @FXML private Button backBtn;

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
        
        // 1. Force the axes to show hours 08:00 - 23:00 immediately
        setupAxes();
        
        // 2. Request data (This ensures it updates every time you enter the screen)
        ClientUI.console.accept(new GetReportsRequest());
    }

    private void setupAxes() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 8; i <= 23; i++) {
            hours.add(String.format("%02d:00", i));
        }
        // Force the X-Axis to use these categories so they always appear
        activityXAxis.setCategories(hours);
        latenessXAxis.setCategories(hours);
    }

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

    @FXML
    void onBackBtnClick(ActionEvent event) {
        // This will now work because we added onAction in FXML
        ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}