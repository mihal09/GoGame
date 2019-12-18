package server;


import java.io.IOException;

class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 2)
            throw new Exception("2 arguments needed");
        try {
            int size = Integer.parseInt(args[0]);
            boolean withBot = Boolean.valueOf(args[1]);
            Server server = new Server(size, withBot);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}