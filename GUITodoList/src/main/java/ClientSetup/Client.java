package ClientSetup;

import Algorithems.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.server.Request;
import com.hit.server.Response;
import com.hit.util.DateTypeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private final String serverAddress;
    private final int serverPort;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new DateTypeAdapter())
                .create();
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to server: " + serverAddress + ":" + serverPort);
    }

    public Response sendRequest(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        System.out.println("Request Sent: " + jsonRequest);
        out.println(jsonRequest);

        socket.setSoTimeout(10000);
        try {
            String jsonResponse = in.readLine();
            if (jsonResponse == null) {
                throw new IOException("Server closed the connection");
            }
            System.out.println("Response Received: " + jsonResponse);
            Response response = gson.fromJson(jsonResponse, Response.class);
            if ("success".equals(response.getStatus()) && response.getData() != null) {
                List<Task> tasks = deserializeTasks((List<?>) response.getData());
                response.setData(tasks);
            }
            if ("error".equals(response.getStatus())) {
                System.err.println("Server returned an error: " + response.getMessage());
            }
            return response;
        } catch (IOException e) {
            System.err.println("Error reading response from server: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Request createRequest(String action, Map<String, Object> body) {
        return new Request(action, body);
    }

    private List<Task> deserializeTasks(List<?> data) {
        String json = gson.toJson(data);
        Type listType = new TypeToken<List<Task>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
