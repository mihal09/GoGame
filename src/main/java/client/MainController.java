package client;

import client.board.Board;
import client.board.Stone;
import client.enums.ColorEnum;
import client.enums.PlayerState;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class MainController {

    private ColorEnum playerColor;
    private Main main;

    private PlayerState state;
    private Client client;

    Board getBoard() {
        return board;
    }

    @FXML
    private Board board;
    MainController(Main main){
        this.main = main;
        client = new Client();
        client.setController(this);
        state = PlayerState.NO_ENEMY;
    }

    PlayerState getState() {
        return state;
    }

    void createBoard(int size){
        board = new Board(size);
    }

    void setState(PlayerState state) {
        this.state = state;
    }

    void setColor(ColorEnum color){
        this.playerColor = color;
    }
    ColorEnum getColor(){ return playerColor; }

    @FXML
    void startClient(String port) {
        // create new thread to handle network communication
        new Thread(() -> {
            System.out.println("Client started.");
            try {
                client.setConnection(port);
                client.play();
            } catch (IOException ex) {
                System.out.println("Connection Error: " + ex);
                Platform.runLater( () -> main.joinUnblock());
            }
        }).start();
    }

    void pass(){
        client.pass(playerColor);
    }
    void surrender(){
        client.surrender(playerColor);
    }

    void drawBoard(){
        drawBackground();
        drawLines();
        drawStones();
    }

    void makeMove(int x, int y){
        if(board.isEmpty(x,y)) {
            client.makeMove(x,y,playerColor);
        }
    }
    void startGame(int size){
        setState(PlayerState.PLAYING);
        createBoard(size);
        main.startGame(size);
    }
    void startAgreeing(){
        System.out.println("LOCAL AGREEMENT STARTED");
        setState(PlayerState.AGREEING);
        main.setAgreementVisibility(true);
        drawBoard();
    }
    void stopAgreeing(){
        setState(PlayerState.PLAYING);
        main.setAgreementVisibility(false);
        drawBoard();
    }
    void endGame(int blackScore, int whiteScore){
        main.endGame(blackScore, whiteScore);
    }
    void endGameSurrender(){
        main.endGameSurrender();
    }
    void territoryAgree() {
        System.out.println("territoryAgree");
        client.territoryAgree(playerColor);
    }
    void territoryDisagree() {
        System.out.println("territoryDisagree");
        client.territoryDisagree(playerColor);
    }
    private void drawBackground() {
        int size = (Board.offset+ Stone.radius)*board.getSize();
        Rectangle rect = new Rectangle(0,0,size,size);
        rect.setFill(Color.web("#e0ac69"));
        board.getChildren().addAll(rect);
    }
    private void drawLines(){
        Color color = Color.web("#462a12");
        int length = (board.getSize()-1)*Stone.radius + (board.getSize()-1)*Board.offset;
        for (int i = 0; i < board.getSize(); i++) {
                Line line = new Line();
                line.setFill(color);
                int startX = Stone.radius + i*(Stone.radius + Board.offset);
                int startY = Stone.radius;
                line.setStartX(startX);
                line.setStartY(startY);
                line.setEndX(startX);
                line.setEndY(startY + length);
                board.getChildren().addAll(line);
        }

        for (int j = 0; j < board.getSize(); j++) {
            Line line = new Line();
            line.setFill(color);
            int startX = Stone.radius;
            int startY = Stone.radius + j*(Stone.radius + Board.offset);
            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX(startX + length);
            line.setEndY(startY);
            board.getChildren().addAll(line);
        }

    }
    private void drawStones() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getStone(i, j) != null) {
                    Stone oldStone = board.getStone(i, j);
                    oldStone.setEffect(null);
                    final Stone stone = new Stone(oldStone);
                    getBoard().setStone(i,j,stone);
                    final int x = i;
                    final int y = j;
                    if(!state.equals(PlayerState.NO_ENEMY)) {
                        stone.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                                if (getState().equals(PlayerState.AGREEING)) {
                                    if (stone.getColor().equals(ColorEnum.EMPTY)|| stone.getColor().equals(ColorEnum.EMPTY_BLACK) || stone.getColor().equals(ColorEnum.EMPTY_WHITE)) {
                                        if (playerColor.equals(ColorEnum.BLACK)) {
                                            if(event.getButton().equals(MouseButton.PRIMARY))
                                                client.markTerritory(x,y,ColorEnum.BLACK);
                                            else if(event.getButton().equals(MouseButton.SECONDARY))
                                                client.markTerritory(x,y,ColorEnum.WHITE);
                                            else
                                                client.markTerritory(x,y,ColorEnum.EMPTY);
                                        }
                                        else {
                                            if(event.getButton().equals(MouseButton.PRIMARY))
                                                client.markTerritory(x,y,ColorEnum.WHITE);
                                            else if(event.getButton().equals(MouseButton.SECONDARY))
                                                client.markTerritory(x,y,ColorEnum.BLACK);
                                            else
                                                client.markTerritory(x,y,ColorEnum.EMPTY);
                                        }
                                    }
                                } else {
                                    makeMove(x, y);
                                    System.out.println("on click");
                                }
                            }
                        });
                        stone.setOnMouseDragOver(new EventHandler<MouseDragEvent>() {
                            public void handle(MouseDragEvent event) {
                                System.out.println("on mouse  over");
                            }
                        });
                    }
                    stone.repaint();
                    board.getChildren().addAll(stone);
                }
            }
        }
    }

}
