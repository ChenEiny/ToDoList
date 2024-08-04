package Algorithems;

import java.util.Comparator;
import java.util.List;

public class IAlgoShortestJobFirst implements IAlgoScheduling {

    @Override
    public List<Task> scheduleTasks(List<Task> tasks) {
        tasks.sort(Comparator
                .comparing(Task::getDuration).reversed()
                .thenComparing(Task::getPriority).reversed()
                .thenComparing(Task::getDeadline));

        // Print out the tasks in scheduled order
        System.out.println("Scheduled tasks by Shortest Job First:");
        for (Task task : tasks) {
            System.out.println(task);
        }

        return tasks;
    }
}
