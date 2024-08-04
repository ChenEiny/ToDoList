package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hit.controller.ToDoListController;
import Algorithems.Task;
import com.hit.util.DateTypeAdapter;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class RequestHandler {

    private ToDoListController toDoListController;
    private Socket someClient;
    private Gson gson;

    public RequestHandler(Socket someClient, ToDoListController toDoListController) {
        this.someClient = someClient;
        this.toDoListController = toDoListController;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    public void process() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            System.out.println("Starting process method in RequestHandler");
            reader = new BufferedReader(new InputStreamReader(someClient.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(someClient.getOutputStream()), true);

            System.out.println("Reader created, checking for input...");
            String jsonRequest;
            while ((jsonRequest = reader.readLine()) != null) {
                System.out.println("Request Received: " + jsonRequest);
                Request request = gson.fromJson(jsonRequest, Request.class);

                String action = request.getAction();
                Response response;

                try {
                    switch (action) {
                        case "addTask":
                            System.out.println("Processing addTask request");
                            response = addTask(request);
                            System.out.println("Request approved: addTask");
                            break;
                        case "getScheduledTasks":
                            System.out.println("Processing getScheduledTasks request");
                            response = getScheduledTasks(request);
                            System.out.println("Request approved: getScheduledTasks");
                            break;
                        case "deleteTask":
                            System.out.println("Processing deleteTask request");
                            response = deleteTask(request);
                            System.out.println("Request approved: deleteTask");
                            break;
                        case "updateTask":
                            System.out.println("Processing updateTask request");
                            response = updateTask(request);
                            System.out.println("Request approved: updateTask");
                            break;
                        default:
                            response = new Response("error", "Unknown action: " + action, null);
                            System.out.println("Request denied: " + action + ". Reason: " + response.getMessage());
                    }
                } catch (Exception e) {
                    System.err.println("Error processing request: " + e.getMessage());
                    e.printStackTrace();
                    response = new Response("error", "Internal server error: " + e.getMessage(), null);
                }

                String jsonResponse = gson.toJson(response);
                System.out.println("Sending response: " + jsonResponse);
                writer.println(jsonResponse);
                writer.flush(); // Ensure the response is sent
                System.out.println("Response Sent");
            }
        } catch (IOException e) {
            System.err.println("Error in RequestHandler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (someClient != null && !someClient.isClosed()) {
                    someClient.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private Response addTask(Request request) {
        try {
            System.out.println("Parsing task from request body");
            Task task = gson.fromJson(gson.toJson(request.getBody().get("task")), Task.class);
            System.out.println("Adding task: " + task);
            toDoListController.addTask(task);
            System.out.println("Task added successfully");
            return new Response("success", "Task added successfully.", null);
        } catch (Exception e) {
            System.err.println("Error in addTask: " + e.getMessage());
            e.printStackTrace();
            return new Response("error", "Failed to add task: " + e.getMessage(), null);
        }
    }

    private Response getScheduledTasks(Request request) {
        try {
            String algorithm = gson.fromJson(gson.toJson(request.getBody().get("algorithm")), String.class);
            toDoListController.setSchedulingAlgorithm(algorithm);
            List<Task> scheduledTasks = toDoListController.getScheduledTasks();
            System.out.println("Scheduled tasks retrieved successfully");
            return new Response("success", "Scheduled tasks retrieved successfully.", scheduledTasks);
        } catch (Exception e) {
            System.err.println("Error in getScheduledTasks: " + e.getMessage());
            e.printStackTrace();
            return new Response("error", "Failed to retrieve scheduled tasks: " + e.getMessage(), null);
        }
    }

    private Response deleteTask(Request request) {
        int taskId = gson.fromJson(gson.toJson(request.getBody().get("taskId")), Integer.class);
        toDoListController.deleteTask(taskId);
        return new Response("success", "Task deleted successfully.", null);
    }

    private Response updateTask(Request request) {
        try {
            Task task = gson.fromJson(gson.toJson(request.getBody().get("task")), Task.class);
            toDoListController.updateTask(task);
            return new Response("success", "Task updated successfully.", null);
        } catch (Exception e) {
            System.err.println("Error in updateTask: " + e.getMessage());
            e.printStackTrace();
            return new Response("error", "Failed to update task: " + e.getMessage(), null);
        }
    }

}
