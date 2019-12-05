package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MainController {

    int round = 0;

    @FXML
    public Board board;
    public MainController(){
        board = new Board();
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

    void putStone(int x, int y){
        if(board.isEmpty(x,y)){
            round++;
            if(round % 2 == 0)
                board.getStone(x,y).setColor(ColorEnum.BLACK);
            else
                board.getStone(x,y).setColor(ColorEnum.WHITE);
            drawStones();
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
                            System.out.println("on click");
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
