package Testing;
import Algorithems.*;
import Algorithems.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class IAlgoSchedulingTest {

    @Test
    public void testShortestJobFirst() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1,"Task 1", 5, Task.createDate(2024, 7, 24, 12, 50), new Time(1, 0, 0)));
        tasks.add(new Task(2,"Task 2", 7, Task.createDate(2024, 8, 31, 14, 30), new Time(1, 45, 0)));
        tasks.add(new Task(3,"Task 3", 3, Task.createDate(2024, 6, 30, 15, 0), new Time(1, 30, 0)));
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        IAlgoScheduling sjf = new IAlgoShortestJobFirst();

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);
        sjf.scheduleTasks(tasks);
        System.setOut(originalOut);
        String actual = outputStream.toString().trim();


        actual = normalizeLineSeparators(actual);
        // Expected output
        String expected = "Scheduled tasks by Shortest Job First:\n" +
                "Task{id:'1',Description='Task 1', Priority=5, Deadline=24/07/2024 12:50, Duration=01:00:00}\n" +
                "Task{id:'3',Description='Task 3', Priority=3, Deadline=30/06/2024 15:00, Duration=01:30:00}\n"+
                "Task{id:'2',Description='Task 2', Priority=7, Deadline=31/08/2024 14:30, Duration=01:45:00}";

        expected = normalizeLineSeparators(expected);

        // Assert
        Assertions.assertEquals(expected, actual);

        System.out.println("Test ShortestJobFirst Passed:");
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + actual);

    }

    @Test
    public void testShortestJobFirstFullCaseTest() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(4,"Task 1", 3, Task.createDate(2024, 7, 24, 12, 50), new Time(2, 0, 0)));
        tasks.add(new Task(5,"Task 2", 1, Task.createDate(2024, 8, 31, 14, 30), new Time(1, 0, 0)));
        tasks.add(new Task(6,"Task 3", 4, Task.createDate(2024, 6, 30, 15, 0), new Time(3, 0, 0)));
        tasks.add(new Task(7,"Task 4", 2, Task.createDate(2024, 9, 1, 10, 0), new Time(1, 0, 0)));
        tasks.add(new Task(8,"Task 5", 5, Task.createDate(2024, 7, 1, 9, 0), new Time(0, 45, 0)));
        tasks.add(new Task(9,"Task 6", 6, Task.createDate(2024, 6, 30, 15, 0), new Time(2, 30, 0)));
        tasks.add(new Task(10,"Task 7", 2, Task.createDate(2024, 6, 30, 15, 0), new Time(1, 30, 0)));

        IAlgoScheduling sjf = new IAlgoShortestJobFirst();

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);
        sjf.scheduleTasks(tasks);
        System.setOut(originalOut);
        String actual = outputStream.toString().trim();

        actual = normalizeLineSeparators(actual);

        // Expected output
        String expected = "Scheduled tasks by Shortest Job First:\n" +
                "Task{id:'8',Description='Task 5', Priority=5, Deadline=01/07/2024 09:00, Duration=00:45:00}\n" +
                "Task{id:'7',Description='Task 4', Priority=2, Deadline=01/09/2024 10:00, Duration=01:00:00}\n" +
                "Task{id:'5',Description='Task 2', Priority=1, Deadline=31/08/2024 14:30, Duration=01:00:00}\n" +
                "Task{id:'10',Description='Task 7', Priority=2, Deadline=30/06/2024 15:00, Duration=01:30:00}\n" +
                "Task{id:'4',Description='Task 1', Priority=3, Deadline=24/07/2024 12:50, Duration=02:00:00}\n" +
                "Task{id:'9',Description='Task 6', Priority=6, Deadline=30/06/2024 15:00, Duration=02:30:00}\n" +
                "Task{id:'6',Description='Task 3', Priority=4, Deadline=30/06/2024 15:00, Duration=03:00:00}";

        expected = normalizeLineSeparators(expected);

        // Assert
        Assertions.assertEquals(expected, actual);

        System.out.println("Test ShortestJobFirstComprehensive Passed:");
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + actual);
    }

    @Test
    public void testEarliestDeadlineFirst() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(11,"Task 1", 3, Task.createDate(2024, 5, 31, 23, 0), new Time(2, 30, 0)));
        tasks.add(new Task(12,"Task 2", 7, Task.createDate(2024, 5, 31, 12, 30), new Time(1, 45, 0)));
        tasks.add(new Task(13,"Task 3", 5, Task.createDate(2024, 5, 31, 12, 29), new Time(3, 0, 0)));

        IAlgoScheduling edf = new IAlgoEarliestDeadlineFirst();

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);
        edf.scheduleTasks(tasks);
        System.setOut(originalOut);
        String actual = outputStream.toString().trim();


        actual = normalizeLineSeparators(actual);

        // Expected output
        String expected = "Scheduled tasks by Earliest Deadline First:\n" +
                "Task{id:'13',Description='Task 3', Priority=5, Deadline=31/05/2024 12:29, Duration=03:00:00}\n" +
                "Task{id:'12',Description='Task 2', Priority=7, Deadline=31/05/2024 12:30, Duration=01:45:00}\n" +
                "Task{id:'11',Description='Task 1', Priority=3, Deadline=31/05/2024 23:00, Duration=02:30:00}";


        expected = normalizeLineSeparators(expected);

        // Assert
        Assertions.assertEquals(expected, actual);

        System.out.println("Test EarliestDeadlineFirst Passed:");
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + actual);
    }

    @Test
    public void testEarliestDeadlineFirstWithSamePriority() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(14,"Task 1", 5, Task.createDate(2024, 5, 31, 23, 0), new Time(2, 30, 0)));
        tasks.add(new Task(15,"Task 2", 5, Task.createDate(2024, 5, 31, 12, 30), new Time(1, 45, 0)));
        tasks.add(new Task(16,"Task 3", 5, Task.createDate(2024, 5, 31, 12, 29), new Time(3, 0, 0)));

        IAlgoScheduling edf = new IAlgoEarliestDeadlineFirst();

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);
        edf.scheduleTasks(tasks);
        System.setOut(originalOut);
        String actual = outputStream.toString().trim();

        actual = normalizeLineSeparators(actual);

        // Expected output
        String expected = "Scheduled tasks by Earliest Deadline First:\n" +
                "Task{id:'16',Description='Task 3', Priority=5, Deadline=31/05/2024 12:29, Duration=03:00:00}\n" +
                "Task{id:'15',Description='Task 2', Priority=5, Deadline=31/05/2024 12:30, Duration=01:45:00}\n" +
                "Task{id:'14',Description='Task 1', Priority=5, Deadline=31/05/2024 23:00, Duration=02:30:00}";

        expected = normalizeLineSeparators(expected);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testLongestProcessingTime() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(17,"Task 1", 5, Task.createDate(2024, 5, 31, 0, 0), new Time(1, 30, 0)));
        tasks.add(new Task(18,"Task 2", 7, Task.createDate(2024, 5, 31, 0, 0), new Time(2, 0, 0)));
        tasks.add(new Task(19,"Task 3", 3, Task.createDate(2024, 5, 31, 0, 0), new Time(1, 45, 0)));

        IAlgoScheduling lpt = new IAlgoLongestProccesingTime();

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);
        lpt.scheduleTasks(tasks);
        System.setOut(originalOut);
        String actual = outputStream.toString().trim();

        actual = normalizeLineSeparators(actual);

        String expected = "Scheduled tasks by Longest Processing Time:\n" +
                "Task{id:'18',Description='Task 2', Priority=7, Deadline=31/05/2024 00:00, Duration=02:00:00}\n" +
                "Task{id:'19',Description='Task 3', Priority=3, Deadline=31/05/2024 00:00, Duration=01:45:00}\n" +
                "Task{id:'17',Description='Task 1', Priority=5, Deadline=31/05/2024 00:00, Duration=01:30:00}";

        expected = normalizeLineSeparators(expected);

        // Assert
        Assertions.assertEquals(expected, actual);

        System.out.println("Test LongestProccesingTime Passed:");
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + actual);
    }

    @Test
    public void testLongestProcessingTimeWithMixedAttributes() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(20,"Task A", 4, Task.createDate(2024, 5, 31, 0, 0), new Time(0, 20, 0)));
        tasks.add(new Task(21,"Task B", 2, Task.createDate(2024, 6, 1, 0, 0), new Time(1, 0, 0)));
        tasks.add(new Task(22,"Task C", 5, Task.createDate(2024, 5, 30, 0, 0), new Time(1, 30, 0)));
        tasks.add(new Task(23,"Task D", 3, Task.createDate(2024, 5, 31, 12, 0), new Time(2, 45, 0)));
        tasks.add(new Task(24,"Task E", 1, Task.createDate(2024, 6, 2, 0, 0), new Time(2, 0, 0)));
        tasks.add(new Task(25,"Task F", 6, Task.createDate(2024, 5, 29, 0, 0), new Time(0, 45, 0)));

        IAlgoScheduling lpt = new IAlgoLongestProccesingTime();

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);
        lpt.scheduleTasks(tasks);
        System.setOut(originalOut);
        String actual = outputStream.toString().trim();

        actual = normalizeLineSeparators(actual);

        // Expected output
        String expected = "Scheduled tasks by Longest Processing Time:\n" +
                "Task{id:'23',Description='Task D', Priority=3, Deadline=31/05/2024 12:00, Duration=02:45:00}\n" +
                "Task{id:'24',Description='Task E', Priority=1, Deadline=02/06/2024 00:00, Duration=02:00:00}\n" +
                "Task{id:'22',Description='Task C', Priority=5, Deadline=30/05/2024 00:00, Duration=01:30:00}\n" +
                "Task{id:'21',Description='Task B', Priority=2, Deadline=01/06/2024 00:00, Duration=01:00:00}\n" +
                "Task{id:'25',Description='Task F', Priority=6, Deadline=29/05/2024 00:00, Duration=00:45:00}\n" +
                "Task{id:'20',Description='Task A', Priority=4, Deadline=31/05/2024 00:00, Duration=00:20:00}";

        expected = normalizeLineSeparators(expected);

        // Assert
        Assertions.assertEquals(expected, actual);

        System.out.println("Test LongestProcessingTimeWithMixedAttributes Passed:");
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + actual);
    }
    private String normalizeLineSeparators(String str) {
        return str.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
    }


}
