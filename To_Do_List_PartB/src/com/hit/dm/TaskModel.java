//Chen Einy 209533785
//Eli Shulman 316040120

package com.hit.dm;

import Algorithems.Task;

import java.sql.Time;
import java.util.Date;

public class TaskModel extends Task {

    public TaskModel(int id,String description, int priority, Date deadline, Time duration) {
        super(id,description, priority, deadline, duration);
    }
}
// list=<task>= list.getduration/deadline