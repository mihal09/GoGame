package sample;
import javafx.application.Platform;
import server.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {
    private static final int port = 8080;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private MainController mainController;
    private boolean success;


    void setController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Sets up networking.
     *
     * @param serverAddress address of the server.
     * @throws IOException is thrown by Socket constructor.
     */

    void setConnection(String serverAddress) throws IOException {
        // Setup networking
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Sends a message to the server.
     *
     * @param message Message to be send.
     */
    void sendMessage(String message) {
        out.println(message);
    }

    void makeMove(int x, int y, ColorEnum color){
        sendMessage("MOVE"+ " "+ x + " " + y + " " +color.toString());
    }

    void markTerritory(int x, int y, ColorEnum color){
        sendMessage("MARK_TERRITORY"+ " "+ x + " " + y + " " +color.toString());
        System.out.println("Sending markTerritory");
    }

    void territoryAgree(ColorEnum color){
        sendMessage("TERRITORY_AGREE" + " " + color.toString());
    }

    void territoryDisagree(ColorEnum color){
        sendMessage("TERRITORY_DISAGREE" + " " + color.toString());
    }

    void surrender(ColorEnum color) { sendMessage("SURRENDER" + " " + color.toString());}

    void pass(ColorEnum color){
        sendMessage("PASS" + " " + color.toString());
    }

    /**
     * Main loop of the client.
     *
     * @throws IOException is thrown by socket.close().
     */
    void play() throws IOException {
        try {
            while (true) {
                final String response = in.readLine();
                if (response != null) {
                    System.out.println("Response from server: " + response);
                    handleResponse(response);
                }
            }
        } finally {
            socket.close();
        }
    }

    /**
     * Handles response from the server.
     *
     * @param response Response read by the BufferedReader.
     */
    private void handleResponse(String response) {
        String[] words = response.split(" ");
        switch (words[0]) {
            case "START_GAME":
                int boardSize = Integer.parseInt(words[1]);
                Platform.runLater(() -> {
                    mainController.startGame(boardSize);
                    mainController.drawBoard();
                });
                break;
            case "WELCOME":
                String color = words[1];
                ColorEnum colorEnum = color.equals("BLACK") ? ColorEnum.BLACK : ColorEnum.WHITE;
                Platform.runLater(() -> {
                    mainController.setColor(colorEnum);
                });
                break;
            case "SURRENDER":
                String surrenderingPlayer = words[1];
                Platform.runLater(() -> {
                    if (!surrenderingPlayer.equals(mainController.getColor().toString()))
                        mainController.endGameSurrend();
                });

            case "SCORE":
                int scoreBlack = Integer.parseInt(words[1]);
                int scoreWhite = Integer.parseInt(words[2]);
                Platform.runLater(() -> {
                    mainController.endGame(scoreBlack, scoreWhite);
                });

                break;
            case "BOARD":
                String sentBoard = words[1];
                int size = mainController.board.getSize();
                for (int x=0; x<size; x++) {
                    for (int y = 0; y < size; y++) {
                        char field = sentBoard.charAt(x * size + y);
                        switch (field) {
                            case 'B':
                                mainController.board.getStone(x, y).setColor(ColorEnum.BLACK);
                                break;
                            case 'W':
                                mainController.board.getStone(x, y).setColor(ColorEnum.WHITE);
                                break;
                            case 'E':
                                mainController.board.getStone(x, y).setColor(ColorEnum.EMPTY);
                                break;
                            case '1':
                                mainController.board.getStone(x, y).setColor(ColorEnum.EMPTY_BLACK);
                                break;
                            case '2':
                                mainController.board.getStone(x, y).setColor(ColorEnum.EMPTY_WHITE);
                                break;
                        }
                    }
                }
                Platform.runLater(() -> mainController.drawBoard());
                break;
            case "INVALID_MOVE":
                System.out.println("ZLY RUCH");
                break;
            case "START_AGREEING":
                Platform.runLater(() -> mainController.startAgreeing());
                break;
            case "TERRITORY_DISAGREEMENT":
                Platform.runLater(() -> mainController.stopAgreeing());
                break;
        }
    }

}