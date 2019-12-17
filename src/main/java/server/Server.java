package server;


import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private static final int port = 8080;
    private int size;
    private boolean withBot;

    public Server(int size, boolean withBot){
        this.size = size;
        this.withBot = withBot;
    }

    public void start() throws IOException {
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("Server is running.");
            new Game(size,withBot,listener);
        }
    }
}