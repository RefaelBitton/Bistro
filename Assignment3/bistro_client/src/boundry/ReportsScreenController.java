package boundry;

import java.util.Map;
import entities.GetReportsRequest;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

public class ReportsScreenController implements IController {
    private User user;

    // Replaced TextAreas with BarCharts
    @FXML private BarChart<String, Number> activityChart; 
    @FXML private BarChart<String, Number> latenessChart; 
    @FXML private Button backBtn;

    @FXML
    public void initialize() {
        ClientUI.console.setController(this);
        // Request report data immediately when screen loads
        ClientUI.console.accept(new GetReportsRequest());
    }

    @Override
    public void setResultText(Object result) {
        // The result is the Map<String, Map<Integer, Double>> we built in DBconnector
        if (result instanceof Map) {
            Map<String, Map<Integer, Double>> data = (Map<String, Map<Integer, Double>>) result;
            
            Platform.runLater(() -> {
                // Populate Graph 1: Activity
                populateActivityChart(data.get("Arrivals"), data.get("Departures"));
                
                // Populate Graph 2: Lateness
                populateLatenessChart(data.get("SubLateness"), data.get("GuestLateness"));
            });
        }
    }

    private void populateActivityChart(Map<Integer, Double> arrivals, Map<Integer, Double> departures) {
        activityChart.getData().clear();
        activityChart.setTitle("Customer Activity by Hour (Arrivals vs Departures)");
        
        XYChart.Series<String, Number> seriesArr = new XYChart.Series<>();
        seriesArr.setName("Arrived");
        
        XYChart.Series<String, Number> seriesDep = new XYChart.Series<>();
        seriesDep.setName("Left");

        // Loop through business hours (e.g., 08:00 to 23:00)
        for (int i = 8; i <= 23; i++) { 
            String hourLabel = String.format("%02d:00", i);
            seriesArr.getData().add(new XYChart.Data<>(hourLabel, arrivals.get(i)));
            seriesDep.getData().add(new XYChart.Data<>(hourLabel, departures.get(i)));
        }
        
        activityChart.getData().addAll(seriesArr, seriesDep);
    }

    private void populateLatenessChart(Map<Integer, Double> subLate, Map<Integer, Double> guestLate) {
        latenessChart.getData().clear();
        latenessChart.setTitle("Average Lateness (Subscribers vs Guests)");

        XYChart.Series<String, Number> seriesSub = new XYChart.Series<>();
        seriesSub.setName("Subscribers");

        XYChart.Series<String, Number> seriesGuest = new XYChart.Series<>();
        seriesGuest.setName("Guests");

        for (int i = 8; i <= 23; i++) {
            String hourLabel = String.format("%02d:00", i);
            seriesSub.getData().add(new XYChart.Data<>(hourLabel, subLate.get(i)));
            seriesGuest.getData().add(new XYChart.Data<>(hourLabel, guestLate.get(i)));
        }
        
        latenessChart.getData().addAll(seriesSub, seriesGuest);
    }

    @FXML
    void onBackBtnClick(ActionEvent event) {
        ClientUI.console.switchScreen(this, event, "/boundry/WorkerScreen.fxml", user);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}