package boundry;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import entities.Order;
import entities.User;
import entities.WriteRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class OrderScreenController implements IController {

    private User user;

    @FXML
    private DatePicker orderDatePicker;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<Integer> guestsComboBox;

    @FXML
    private TextArea resultTxt;

    @FXML
    private Button OrderBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    public void initialize() {

        ClientUI.console.setController(this);

        // Guests: 1–20
        for (int i = 1; i <= 20; i++) {
            guestsComboBox.getItems().add(i);
        }

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusMonths(1);

        // Restrict date picker
        orderDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(today) || date.isAfter(maxDate)) {
                    setDisable(true);
                }
            }
        });

        // Update time options when date changes
        orderDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            updateAvailableTimes(newDate);
        });
    }

    private void updateAvailableTimes(LocalDate selectedDate) {

        timeComboBox.getItems().clear();
        if (selectedDate == null) return;

        LocalTime opening = LocalTime.of(11, 0);
        LocalTime closing = LocalTime.of(22, 0);

        LocalDateTime nowPlusHour = LocalDateTime.now().plusHours(1);

        for (LocalTime time = opening; time.isBefore(closing); time = time.plusMinutes(30)) {

            LocalDateTime candidate = LocalDateTime.of(selectedDate, time);

            // Only restrict time if the date IS today
            if (selectedDate.equals(LocalDate.now())) {
                if (candidate.isBefore(nowPlusHour)) continue;
            }

            timeComboBox.getItems().add(time.toString());
        }
    }

    @FXML
    void OnOrderClick(ActionEvent event) {

        resultTxt.clear();

        try {
            LocalDate date = orderDatePicker.getValue();
            String time = timeComboBox.getValue();
            Integer guests = guestsComboBox.getValue();

            if (date == null || time == null || guests == null) {
                throw new IllegalArgumentException();
            }

            ArrayList<String> args = new ArrayList<>();
            args.add(date.toString());
            args.add(time);
            args.add(guests.toString());

            WriteRequest request = new WriteRequest(new Order(args));
            ClientUI.console.accept(request);

        } catch (Exception e) {
            resultTxt.setText(
                "❌ Invalid input.\n\n" +
                "• Select a valid date (today – 1 month ahead)\n" +
                "• If today: select time at least 1 hour from now\n" +
                "• Select number of guests (1–20)"
            );
        }
    }

    @FXML
    void onCancelClick(ActionEvent event) throws IOException {
        ClientUI.console.switchScreen(this, event, "/boundry/mainScreen.fxml", user);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setResultText(String result) {
        resultTxt.setText(result);
    }
}
