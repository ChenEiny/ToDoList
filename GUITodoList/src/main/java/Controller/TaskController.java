package Controller;

import Algorithems.Task;
import com.hit.dao.DaoFileImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.TaskModel;
import com.hit.service.ToDoListService;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskController {

    @FXML
    private TableView<TaskModel> taskTable;
    @FXML
    private TableColumn<TaskModel, Integer> idColumn;
    @FXML
    private TableColumn<TaskModel, String> descriptionColumn;
    @FXML
    private TableColumn<TaskModel, Integer> priorityColumn;
    @FXML
    private TableColumn<TaskModel, Date> deadlineColumn;
    @FXML
    private TableColumn<TaskModel, Time> durationColumn;

    private ToDoListService toDoListService;

    public TaskController() {
        this.toDoListService = new ToDoListService(new DaoFileImpl());
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        loadTasks();
    }

    public void addTask(Task newTask) {
        toDoListService.addTask(newTask);
        loadTasks();
    }

    public void deleteTask(int taskId) {
        toDoListService.deleteTask(taskId);
        loadTasks();
    }

    public void updateTask(Task updatedTask) {
        toDoListService.updateTask(updatedTask);
        loadTasks();
    }

    public List<Task> getAllTasks() {
        return toDoListService.getAllTasks();
    }

    private void loadTasks() {
        if (taskTable != null) {
            List<Task> tasks = getAllTasks();
            ObservableList<TaskModel> taskList = FXCollections.observableArrayList();

            for (Task task : tasks) {
                TaskModel taskModel = new TaskModel(
                        task.getId(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getDeadline(), // Keep Date type
                        task.getDuration()  // Keep Time type
                );
                taskList.add(taskModel);
            }

            taskTable.setItems(taskList);
        } else {
            System.err.println("TaskTable is not initialized.");
        }
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
