package Model;

import java.sql.Time;
import java.util.Date;
import Algorithems.Task;

public class TaskModel extends Task {

    public TaskModel(int id, String description, int priority, Date deadline, Time duration) {
        super(id, description, priority, deadline, duration);
    }
}
