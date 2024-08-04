import Algorithems.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Example usage
//        Date deadline = new Date(); // current date/time
//        Time duration = new Time(2, 30, 0); // 2 hours 30 minutes
//        Algorithems.Task task = new Algorithems.Task("Complete assignment", 5, deadline, duration);
//
//        System.out.println(task);

        List<Task> tasks = new ArrayList<>();

        Date deadline1 = Task.createDate(2025, 7, 21, 23, 0); // July 21, 2025, 00:00
        Date deadline2 = Task.createDate(2025, 7, 22, 0, 0);
        Date deadline3 = Task.createDate(2025, 7, 21, 0, 0);

        // Adding tasks with Date deadlines
        tasks.add(new Task(1,"Complete Todo", 5, deadline1, new Time(2, 30, 0)));
        tasks.add(new Task(2,"Ahlha Nissim", 3, deadline2, new Time(1, 45, 0)));
        tasks.add(new Task(3,"Write report", 7, deadline3, new Time(3, 30, 0)));


        IAlgoScheduling edf = new IAlgoEarliestDeadlineFirst();
        IAlgoScheduling lpt= new IAlgoLongestProccesingTime();
        IAlgoScheduling sjf= new IAlgoShortestJobFirst();


       System.out.println("Earliest Deadline First:");
       edf.scheduleTasks(tasks);

    }

}