package Algorithems;

import java.util.Comparator;
import java.util.List;

public class IAlgoEarliestDeadlineFirst implements IAlgoScheduling {

    @Override
    public List<Task> scheduleTasks(List<Task> tasks) {
        tasks.sort(Comparator
                .comparing(Task::getDeadline).reversed()
                .thenComparing(Task::getPriority).reversed()
                .thenComparing(Task::getDuration));

        // Print out the tasks in scheduled order
        System.out.println("Scheduled tasks by Earliest Deadline First:");
        for (Task task : tasks) {
            System.out.println(task);
        }

        return tasks;
    }
}
