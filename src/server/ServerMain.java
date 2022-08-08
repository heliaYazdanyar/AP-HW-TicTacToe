package server;

import java.io.IOException;

public class ServerMain {

    static int serverPort = 9999;

    public static void main(String[] args) throws IOException {
        serverPort=Config.getPort();

        new GameServer(serverPort).start();
    }
}
