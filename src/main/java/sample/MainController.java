package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import server.Player;

import java.io.IOException;

public class MainController {

    private ColorEnum playerColor;
    private Main main;

    private PlayerState state;
    private Client client;

    @FXML
    public Board board;
    public MainController(Main main){
        this.main = main;
        this.playerColor = playerColor;
        client = new Client();
        client.setController(this);
        state = PlayerState.NO_ENEMY;
    }

    public PlayerState getState() {
        System.out.println(state);
        return state;
    }

    public void createBoard(int size){
        board = new Board(size);
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setColor(ColorEnum color){
        this.playerColor = color;
    }
    public ColorEnum getColor(){ return playerColor; }

    @FXML
    public void startClient(String port) {
        // create new thread to handle network communication
        new Thread(() -> {
            System.out.println("Client started.");
            try {
                client.setConnection(port);
                client.play();
            } catch (IOException ex) {
                System.out.println("Connection Error: " + ex);
            }
        }).start();
    }

    public void pass(){
        client.pass(playerColor);
    }

    public void clearBoard(){
        board.clearStones();
        drawBoard();
    }

    public void drawBoard(){
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

    public void territoryAgree() {
        System.out.println("territoryAgree");
        client.territoryAgree(playerColor);
    }

    public void territoryDisagree() {
        System.out.println("territoryDisagree");
        client.territoryDisagree(playerColor);
    }


    private void drawBackground() {
        int size = (Board.offset+Stone.radius)*board.getSize();
        Rectangle rect = new Rectangle(0,0,size,size);
        rect.setFill(Color.web("#e0ac69"));
        board.getChildren().addAll(rect);
    }

    public void drawLines(){
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

    public void drawStones() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getStone(i, j) != null) {
                    Stone oldStone = board.getStone(i, j);
                    oldStone.setEffect(null);
                    final Stone stone = new Stone(oldStone);
                    board.stones[i][j] = stone;
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
