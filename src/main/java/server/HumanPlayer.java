package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HumanPlayer extends Player {
    BufferedReader input;
    PrintWriter output;
    public ProtocolServer protocol;
    private Game game;
    private final Socket socket;

    HumanPlayer(ColorEnum color, Socket socket, Game game) {
        alive = true;
        this.game = game;
        this.color = color;
        this.socket = socket;

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            protocol = new ProtocolServer(this.output);

            output.println("");
            output.println("WELCOME " + this.color);
            output.println("Waiting for your opponent to connect...");
        } catch (IOException ex) {
            System.out.println("server.Game.Player error: " + ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("All players connected");
            protocol.startGame(game.getController().getBoard());
            while (alive) {
                String command = input.readLine();
                if (command != null) {
                    game.handleCommand(command);
                }
            }
        } catch (IOException ex) {
            System.out.println("server.Game.Player error: " + ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
