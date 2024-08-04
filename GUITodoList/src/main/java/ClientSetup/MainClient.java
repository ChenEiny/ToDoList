package ClientSetup;

import Algorithems.Task;
import com.hit.server.Request;
import com.hit.server.Response;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 12345);
        client.connect();

        Map<String, Object> body = new HashMap<>();
        body.put("task", new Task(1, "Test Task", 5, new Date(), new Time(0)));
        Request request = client.createRequest("addTask", body);

        Response response = client.sendRequest(request);
        System.out.println("Response from server: " + response.getMessage());
    }
}
