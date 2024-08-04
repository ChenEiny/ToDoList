//Chen Einy 209533785
//Eli Shulman 316040120

package com.hit.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Algorithems.Task;

public class DaoFileImpl implements IDao {
    private final String filePath;

    public DaoFileImpl() {
        this.filePath = "DataSource.txt";
    }

    @Override
    public void saveTask(Task task) {
        List<Task> tasks = getAllTasks();

        try {
            for (Task existingTask : tasks) {
                if (existingTask.getId() == task.getId() || existingTask.getDescription().equals(task.getDescription())) {
                    throw new TaskAlreadyExistsException("Task with the same ID or description already exists.");
                }
            }

            tasks.add(task);
            writeTasksToFile(tasks);
            System.out.println("Task added successfully.");

        } catch (TaskAlreadyExistsException e) {
            System.err.println(e.getMessage());
        }
    }


    public void deleteTask(int taskId) {
        List<Task> tasks = getAllTasks();
        boolean isRemoved = tasks.removeIf(task -> task.getId() == taskId);

        if (isRemoved) {
            writeTasksToFile(tasks);
            System.out.println("Task with ID " + taskId + " deleted successfully.");
        } else {
            System.out.println("Task with ID " + taskId + " does not exist.");
        }

    }


    @Override
    public void updateTask(Task task) {
        List<Task> tasks = getAllTasks();
        boolean isUpdated = false;

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            writeTasksToFile(tasks);
            System.out.println("Task with ID " + task.getId() + " updated successfully.");
        } else {
            System.out.println("Task with ID " + task.getId() + " not found. Update failed.");
        }

    }

    @Override
    public Task getTask(int taskId) {
        List<Task> tasks = getAllTasks();
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            tasks = (List<Task>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error occurred while reading tasks from file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Class not found while reading tasks from file: " + e.getMessage());
        }
        return tasks;
    }


    private void writeTasksToFile(List<Task> tasks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("IOException occurred while reading tasks from file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
