//Chen Einy 209533785
//Eli Shulman 316040120

import com.hit.server.Server;

import java.io.IOException;

public class ServerDriver {
    public static void main(String[] args) throws IOException {
        Server server = new Server(12345);
        new Thread(server).start();
    }
}
