package server;


import java.io.IOException;

class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(19, false);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}