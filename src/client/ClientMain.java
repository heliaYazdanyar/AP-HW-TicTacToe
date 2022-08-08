package client;

import server.Config;
import java.io.IOException;

public class ClientMain {
    static String serverIP = "localhost";
    static int serverPort;

    public static void main(String[] args) throws IOException {
        serverPort= Config.getPort();

        new Client(serverIP,serverPort).start();
    }


}
