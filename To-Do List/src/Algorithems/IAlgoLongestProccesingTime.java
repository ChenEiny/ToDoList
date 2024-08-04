package Algorithems;

import java.util.Comparator;
import java.util.List;

public class IAlgoLongestProccesingTime implements IAlgoScheduling {

    @Override
    public List<Task> scheduleTasks(List<Task> tasks) {
        tasks.sort(Comparator
                .comparing(Task::getDuration)
                .thenComparing(Task::getPriority).reversed()
                .thenComparing(Task::getDeadline));

        // Print out the tasks in scheduled order
        System.out.println("Scheduled tasks by Longest Processing Time:");
        for (Task task : tasks) {
            System.out.println(task);
        }

        return tasks;
    }
}
