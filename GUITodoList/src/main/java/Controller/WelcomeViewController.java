package Controller;

import ClientSetup.Client;
import View.WelcomeView;
import Algorithems.Task;
import com.hit.server.Request;
import com.hit.server.Response;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WelcomeViewController {

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priorityField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private TextField durationField;

    private Client client;

    @FXML
    public void initialize() {
        // Client will be set after the FXMLLoader loads the FXML and WelcomeView calls setClient()
        System.out.println("Controller initialized. Waiting for client.");
    }

    public void setClient(Client client) {
        this.client = client;
        System.out.println("Client set in controller: " + (client != null ? "Initialized" : "Not Initialized"));
    }

    @FXML
    private void handleSubmit() {
        String description = descriptionField.getText();
        String priorityText = priorityField.getText();
        Date deadline = java.sql.Date.valueOf(deadlinePicker.getValue());
        String durationText = durationField.getText();

        if (client == null) {
            showAlert(AlertType.ERROR, "Client not initialized.");
            return;
        }

        if (description.isEmpty() || priorityText.isEmpty() || deadline == null || durationText.isEmpty()) {
            showAlert(AlertType.ERROR, "All fields must be filled");
            return;
        }

        int priority;
        try {
            priority = Integer.parseInt(priorityText);
            if (priority < 1 || priority > 10) {
                showAlert(AlertType.ERROR, "Priority must be between 1 and 10");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Priority must be a number between 1 and 10");
            return;
        }

        Time duration;
        try {
            duration = Time.valueOf(durationText + ":00");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Duration must be in the format HH:MM");
            return;
        }

        Task task = new Task(generateUniqueId(), description, priority, deadline, duration);

        // Send the task to the server
        sendTaskToServer(task);

        clearFields();
    }

    private void sendTaskToServer(Task task) {
        if (client == null) {
            System.err.println("Client not initialized.");
            return;
        }

        try {
            // Create the request to add the task
            Map<String, Object> body = new HashMap<>();
            body.put("task", task);

            Request request = client.createRequest("addTask", body);
            Response response = client.sendRequest(request);
            System.out.println("Server Response: " + response.getMessage());

            // Optionally, you can handle the response to show confirmation or errors
            showAlert(AlertType.INFORMATION, "Task added successfully.");
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Failed to send task to server.");
            System.err.println("Failed to send task: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        descriptionField.clear();
        priorityField.clear();
        deadlinePicker.setValue(null);
        durationField.clear();
    }

    @FXML
    private void handleReport() {
        // Switch to the Report View
        WelcomeView.switchToReportView();
    }

    private int generateUniqueId() {
        return (int) (Math.random() * 10000); // Example implementation
    }
}