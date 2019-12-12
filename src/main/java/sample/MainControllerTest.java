package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import server.Controller;

public class MainControllerTest {

    int round = 0;

    @FXML
    public Board board;
    public Controller controller;

    public MainControllerTest(){
        board = new Board();
        controller = new Controller(board.getSize());
    }

    public void clearBoard(){
        board.clearStones();
        controller = new Controller(board.getSize());
        drawBoard();
    }

    public void drawBoard(){
        updateStones();
        drawBackground();
        drawLines();
        drawStones();
    }

    void updateStones(){
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getStone(i, j) != null) {
                    Stone oldStone = board.getStone(i, j);

                    ColorEnum colorClient;
                    if (controller.getBoard().getField(i, j).getColor().equals(server.ColorEnum.WHITE))
                        colorClient = ColorEnum.WHITE;
                    else if (controller.getBoard().getField(i, j).getColor().equals(server.ColorEnum.BLACK))
                        colorClient = ColorEnum.BLACK;
                    else
                        colorClient = ColorEnum.EMPTY;
                    oldStone.setColor(colorClient);
                }
            }
        }
    }

    void putStone(int x, int y){
            ColorEnum color = round % 2 == 0 ? ColorEnum.BLACK : ColorEnum.WHITE;
            server.ColorEnum colorServer = color == ColorEnum.WHITE ? server.ColorEnum.WHITE : server.ColorEnum.BLACK;
            if(controller.isMoveLegal(x,y)){
                round++;
                drawBoard();
            }
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
                    stone.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            putStone(x,y);
                        }
                    });
                    stone.setOnMouseDragOver(new EventHandler<MouseDragEvent>() {
                        public void handle(MouseDragEvent event) {
                            System.out.println("on mouse  over");
                        }
                    });
                    stone.repaint();
                    board.getChildren().addAll(stone);
                }
            }
        }
    }
}
