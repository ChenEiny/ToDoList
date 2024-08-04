package Controller;

import ClientSetup.Client;
import View.WelcomeView;
import com.hit.server.Request;
import com.hit.server.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Algorithems.Task;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportViewController {

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, Integer> priorityColumn;

    @FXML
    private TableColumn<Task, java.util.Date> deadlineColumn;

    @FXML
    private TableColumn<Task, java.sql.Time> durationColumn;

    @FXML
    private TableColumn<Task, Void> completeColumn;

    @FXML
    private TableColumn<Task, Void> deleteColumn;

    @FXML
    private TableColumn<Task, Void> updateColumn;

    private Client client;
    private Map<Integer, Boolean> taskCompletionMap = new HashMap<>();

    @FXML
    public void initialize() {
        algorithmComboBox.getItems().addAll("EarliestDeadLineFirst", "LongestProccesingTime", "ShortestJobFirst");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        addDeleteButtonToTable();
        addUpdateButtonToTable();
        addCompleteCheckboxToTable();
    }

    @FXML
    private void handleSubmitReport() {
        String selectedAlgorithm = algorithmComboBox.getValue();
        System.out.println("Selected Algorithm: " + selectedAlgorithm);
        if (selectedAlgorithm == null) {
            showAlert(Alert.AlertType.ERROR, "You must select an algorithm");
            return;
        }

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("algorithm", selectedAlgorithm);

            Request request = client.createRequest("getScheduledTasks", body);
            Response response = client.sendRequest(request);

            if ("success".equals(response.getStatus())) {
                List<Task> tasks = (List<Task>) response.getData();
                showTasks(tasks);
            } else {
                showAlert(Alert.AlertType.ERROR, response.getMessage());
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to get tasks from server.");
            e.printStackTrace();
        }
    }

    private void showTasks(List<Task> tasks) {
        // Preserve current checkbox states
        for (Task task : taskTable.getItems()) {
            taskCompletionMap.put(task.getId(), taskCompletionMap.getOrDefault(task.getId(), false));
        }

        ObservableList<Task> taskList = FXCollections.observableArrayList(tasks);
        taskTable.setItems(taskList);

        // Restore checkbox states
        for (Task task : tasks) {
            taskCompletionMap.putIfAbsent(task.getId(), false);
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<Task, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    handleDelete(task);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void addUpdateButtonToTable() {
        updateColumn.setCellFactory(param -> new TableCell<Task, Void>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    handleUpdate(task);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });
    }

    private void addCompleteCheckboxToTable() {
        completeColumn.setCellFactory(param -> new TableCell<Task, Void>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    taskCompletionMap.put(task.getId(), checkBox.isSelected());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Task task = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(taskCompletionMap.getOrDefault(task.getId(), false));
                    setGraphic(checkBox);
                }
            }
        });
    }

    private void handleDelete(Task task) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("taskId", task.getId());

            Request request = client.createRequest("deleteTask", body);
            Response response = client.sendRequest(request);

            if ("success".equals(response.getStatus())) {
                taskTable.getItems().remove(task);
                taskCompletionMap.remove(task.getId());
                showAlert(Alert.AlertType.INFORMATION, "Task deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, response.getMessage());
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to delete task.");
            e.printStackTrace();
        }
    }

    private void handleUpdate(Task task) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Update Task");
        dialog.setHeaderText("Update the details of the task.");

        // Set the button types.
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create the labels and fields for the task details.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField descriptionField = new TextField();
        descriptionField.setText(task.getDescription());
        TextField priorityField = new TextField();
        priorityField.setText(String.valueOf(task.getPriority()));
        TextField deadlineField = new TextField();
        deadlineField.setText(new SimpleDateFormat("MMM dd, yyyy").format(task.getDeadline()));
        TextField durationField = new TextField();
        durationField.setText(task.getDuration().toString());

        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(new Label("Priority:"), 0, 1);
        grid.add(priorityField, 1, 1);
        grid.add(new Label("Deadline (MMM dd, yyyy):"), 0, 2);
        grid.add(deadlineField, 1, 2);
        grid.add(new Label("Duration (hh:mm:ss):"), 0, 3);
        grid.add(durationField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a task object when the update button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    task.setDescription(descriptionField.getText());
                    task.setPriority(Integer.parseInt(priorityField.getText()));
                    task.setDeadline(new SimpleDateFormat("MMM dd, yyyy").parse(deadlineField.getText()));
                    task.setDuration(Time.valueOf(durationField.getText()));
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Invalid input: " + e.getMessage());
                    return null;
                }
                return new Pair<>(descriptionField.getText(), priorityField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(taskDetails -> {
            // Send update request to server
            try {
                Map<String, Object> body = new HashMap<>();
                body.put("task", task);

                Request request = client.createRequest("updateTask", body);
                Response response = client.sendRequest(request);

                if ("success".equals(response.getStatus())) {
                    taskTable.refresh(); // Refresh the table to show updated task
                    showAlert(AlertType.INFORMATION, "Task updated successfully.");
                } else {
                    showAlert(AlertType.ERROR, response.getMessage());
                }
            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Failed to update task.");
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleAddTask() {
        WelcomeView.switchToWelcomeView();
    }

    public void setClient(Client client) {
        this.client = client;
        System.out.println("Client set in ReportViewController: " + (client != null ? "Initialized" : "Not Initialized"));
    }
}
