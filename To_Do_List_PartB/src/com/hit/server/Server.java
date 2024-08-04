package com.hit.server;

import com.hit.controller.ToDoListController;
import com.hit.dao.DaoFileImpl;
import com.hit.service.ToDoListService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    ServerSocket server;
    public Server(int socketNumber) throws IOException {
        this.server=new ServerSocket(socketNumber);
    }

    @Override
    public void run() {
        try {
            System.out.println("Server Up and Running on port " + server.getLocalPort());
            ToDoListController toDoListController = new ToDoListController(new ToDoListService(new DaoFileImpl()));
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket someClient = server.accept();
                System.out.println("Client connected: " + someClient.getInetAddress() + ":" + someClient.getPort());
                RequestHandler request = new RequestHandler(someClient, toDoListController);
                System.out.println("Processing request...");
                request.process();
                System.out.println("Request Fulfilled");
            }
        } catch (Exception e) {
            System.err.println("Error in Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

