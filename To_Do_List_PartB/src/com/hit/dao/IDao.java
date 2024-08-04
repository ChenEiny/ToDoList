//Chen Einy 209533785
//Eli Shulman 316040120

package com.hit.dao;

import java.util.List;
import Algorithems.Task;

public interface IDao {
    void saveTask(Task task);
    void deleteTask(int taskId);
    void updateTask(Task task);
    Task getTask(int taskId);
    List<Task> getAllTasks();
}
