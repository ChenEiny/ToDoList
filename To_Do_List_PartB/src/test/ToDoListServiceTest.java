//Chen Einy 209533785
//Eli Shulman 316040120

package test;

import Algorithems.*;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.service.ToDoListService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ToDoListServiceTest {

    private ToDoListService toDoListService;
    private IDao taskDao;

    @Before
    public void setUp() throws IOException {
        // Initialize the taskDao
        taskDao = new DaoFileImpl();
        IAlgoScheduling schedulingAlgorithm = new IAlgoShortestJobFirst(); // Assuming you have a concrete implementation class
        toDoListService = new ToDoListService(taskDao);
        toDoListService.setSchedulingAlgorithm(schedulingAlgorithm);
    }




    @Test
    public void testAddTask() {
        // Create a sample task
        Task task = new Task(2, "Task 2", 7, Task.createDate(2024, 8, 31, 14, 30), new Time(1, 45, 0));

        toDoListService.addTask(task);

        Task retrievedTask = taskDao.getTask(2);
        System.out.println(retrievedTask);
        assertNotNull("Retrieved task should not be null", retrievedTask);

        assertEquals("Task IDs should match", task.getId(), retrievedTask.getId());
        assertEquals("Task descriptions should match", task.getDescription(), retrievedTask.getDescription());
        assertEquals("Task priorities should match", task.getPriority(), retrievedTask.getPriority());
        assertEquals("Task durations should match", task.getDuration(), retrievedTask.getDuration());

        String expectedDeadline = task.getDeadline().toString().substring(0, 5); // Extract hour and minute
        String actualDeadline = retrievedTask.getDeadline().toString().substring(0, 5); // Extract hour and minute
        assertEquals("Task deadlines should match", expectedDeadline, actualDeadline);
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task(3, "Task to be deleted", 3, new Date(), new Time(1, 0, 0));
        taskDao.saveTask(task);
        toDoListService.deleteTask(3);

        // Attempt to retrieve the deleted task
        Task deletedTask = taskDao.getTask(3);

        // Assert that the deleted task is null
        assertNull("Deleted task should be null", deletedTask);

    }

    @Test
    public void testUpdateTask() {
        // Add a sample task to be updated
        Task task = new Task(4, "Task to be updated", 7, Task.createDate(2024, 8, 31, 14, 30), new Time(1, 45, 0));
        taskDao.saveTask(task);

        // Create an updated version of the task
        Task updatedTask = new Task(4, "Updated Task", 8, Task.createDate(2024, 9, 1, 15, 30), new Time(2, 30, 0));

        // Update the task in the service
        toDoListService.updateTask(updatedTask);

        // Retrieve the updated task from the service
        Task retrievedTask = taskDao.getTask(4);

        // Assert that the retrieved task is not null
        assertNotNull("Retrieved task should not be null", retrievedTask);

        // Compare fields of updated task
        assertEquals("Task descriptions should match", updatedTask.getDescription(), retrievedTask.getDescription());
        assertEquals("Task priorities should match", updatedTask.getPriority(), retrievedTask.getPriority());
        assertEquals("Task deadlines should match", updatedTask.getDeadline(), retrievedTask.getDeadline());
        assertEquals("Task durations should match", updatedTask.getDuration(), retrievedTask.getDuration());
        System.out.println("Expected Task Description: " + task.getDescription());
        System.out.println("Actual Task Description: " + retrievedTask.getDescription());
    }

    @Test
    public void testGetTask() {
        Task task = new Task(5, "New Task ", 5, Task.createDate(2024, 8, 31, 14, 30), new Time(0, 30, 0));
        taskDao.saveTask(task);

        // Retrieve the added task from the service
        Task retrievedTask = taskDao.getTask(5);

        System.out.println("Expected Task Description: " + task.getDescription());
        System.out.println("Actual Task Description: " + retrievedTask.getDescription());

        // Assert that the retrieved task is not null and matches the added task
        assertNotNull("Retrieved task should not be null", retrievedTask);
        assertEquals("Task IDs should match", task.getId(), retrievedTask.getId());
        assertEquals("Task descriptions should match", task.getDescription(), retrievedTask.getDescription());
        assertEquals("Task priorities should match", task.getPriority(), retrievedTask.getPriority());

        // Extract hour and minute parts of the deadlines
        int expectedHour = task.getDeadline().getHours();
        int expectedMinute = task.getDeadline().getMinutes();
        int actualHour = retrievedTask.getDeadline().getHours();
        int actualMinute = retrievedTask.getDeadline().getMinutes();

        // Compare only hour and minute parts
        assertEquals("Task deadline hours should match", expectedHour, actualHour);
        assertEquals("Task deadline minutes should match", expectedMinute, actualMinute);

        assertEquals("Task durations should match", task.getDuration(), retrievedTask.getDuration());
    }




    @Test
    public void testGetAllTasks() {
        // Add multiple sample tasks
        Task task1 = new Task(6, "Task 6", 5, Task.createDate(2024, 8, 31, 14, 30), new Time(0, 30, 0));
        Task task2 = new Task(7, "Task 7", 3, Task.createDate(2024, 8, 31, 14, 30), new Time(1, 0, 0));
        Task task3 = new Task(8, "Task 8", 7, Task.createDate(2024, 8, 31, 14, 30), new Time(2, 0, 0));
        taskDao.saveTask(task1);
        taskDao.saveTask(task2);
        taskDao.saveTask(task3);

        // Retrieve all tasks from the service
        List<Task> allTasks = toDoListService.getAllTasks();

        // Assert that the retrieved list is not null
        assertNotNull("List of tasks should not be null", allTasks);

        // Assert that the retrieved list contains all added tasks
        assertTrue("List of tasks should contain task 6", allTasks.stream().anyMatch(t -> t.getId() == task1.getId()));
        assertTrue("List of tasks should contain task 7", allTasks.stream().anyMatch(t -> t.getId() == task2.getId()));
        assertTrue("List of tasks should contain task 8", allTasks.stream().anyMatch(t -> t.getId() == task3.getId()));

        // Print expected and actual results
        System.out.println("Expected:");
        System.out.println("Task 6: " + task1);
        System.out.println("Task 7: " + task2);
        System.out.println("Task 8: " + task3);

        System.out.println("Actual:");
        for (Task task : allTasks) {
            System.out.println(task);
        }
    }


    @Test
    public void testShortestJobFirstSchedulingAlgorithm() throws IOException {
        // Add tasks to the to-do list
        Files.write(Paths.get("DataSource.txt"), "".getBytes());

        Task task1 = new Task(11, "Task 11", 5, Task.createDate(2024, 8, 31, 14, 30), new Time(0, 30, 0));
        Task task2 = new Task(12, "Task 12", 3, Task.createDate(2024, 8, 31, 14, 30), new Time(1, 0, 0));
        Task task3 = new Task(13, "Task 13", 7, Task.createDate(2024, 8, 31, 14, 30), new Time(2, 0, 0));
        taskDao.saveTask(task1);
        taskDao.saveTask(task2);
        taskDao.saveTask(task3);

        List<Task> scheduledTasks = toDoListService.getScheduledTasks();

        // Verify the output of the scheduling algorithm
        assertEquals("Task 11", scheduledTasks.get(0).getDescription());
    }


}
