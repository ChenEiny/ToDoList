package com.hit.service;

import Algorithems.*;
import com.hit.dao.IDao;

import java.util.List;

public class ToDoListService {
    private IDao taskDao;
    private IAlgoScheduling schedulingAlgorithm;

    public ToDoListService(IDao taskDao) {
        this.taskDao = taskDao;
    }

    public void addTask(Task task) {
        taskDao.saveTask(task);
    }

    public void deleteTask(int taskId) {
        taskDao.deleteTask(taskId);
    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }

    public Task getTask(int taskId) {
        return taskDao.getTask(taskId);
    }

    public List<Task> getAllTasks() {
        return taskDao.getAllTasks();
    }


    public List<Task> getScheduledTasks() {
        if (schedulingAlgorithm == null) {
            throw new IllegalStateException("Scheduling algorithm not set");
        }
        List<Task> tasks = taskDao.getAllTasks();
        return schedulingAlgorithm.scheduleTasks(tasks);
    }

    public void setSchedulingAlgorithm(IAlgoScheduling schedulingAlgorithm) {
        this.schedulingAlgorithm = schedulingAlgorithm;
    }
}
