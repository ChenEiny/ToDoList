package com.hit.controller;

import Algorithems.*;
import com.hit.service.ToDoListService;

import java.util.List;

public class ToDoListController {

    private ToDoListService toDoService;

    public ToDoListController(ToDoListService toDoService) {
        this.toDoService = toDoService;
    }

    public void addTask(Task task) {
        toDoService.addTask(task);
    }

    public void deleteTask(int taskId) {
        toDoService.deleteTask(taskId);
    }

    public void updateTask(Task task) {
        toDoService.updateTask(task);
    }

    public Task getTask(int taskId) {
        return toDoService.getTask(taskId);
    }

    public List<Task> getAllTasks() {
        return toDoService.getAllTasks();
    }

    public List<Task> getScheduledTasks() {
        return toDoService.getScheduledTasks();
    }

    public void setSchedulingAlgorithm(String schedulingAlgorithm) {
        switch (schedulingAlgorithm) {
            case "ShortestJobFirst":
                toDoService.setSchedulingAlgorithm(new IAlgoShortestJobFirst());
                break;
            case "EarliestDeadLineFirst":
                toDoService.setSchedulingAlgorithm(new IAlgoEarliestDeadlineFirst());
                break;
            case "LongestProccesingTime":
                toDoService.setSchedulingAlgorithm(new IAlgoLongestProccesingTime());
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm name: " + schedulingAlgorithm);
        }
    }
}
