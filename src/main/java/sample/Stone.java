package sample;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Stone extends Circle {

    // (X,Y) on board
    private int x;
    private int y;
    private ColorEnum color;
    public final static int radius = 25;

    Stone(int x, int y, ColorEnum color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    Stone(Stone stone){
        x = stone.x;
        y = stone.y;
        color = stone.color;
    }

    public void repaint() {
        setVisible(true);
        if(color == ColorEnum.BLACK) {
            setFill(Color.web("#1b1b1b"));
        }
        else if(color == ColorEnum.WHITE)
            setFill(Color.WHITE);
        else {
            setFill(Color.GRAY);
            //setVisible(false);
            setOpacity(0);
        }
        this.setCenterX(x);
        this.setCenterY(y);
        setRadius(radius);
    }

    /**
     * color getter.
     * @return this.color
     */
    public ColorEnum getColor() {
        return color;
    }

    public void setColor(ColorEnum color){
        this.color = color;
    }

    public int getX() {
        return (x-20)/(2*radius);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (y-20)/radius;
    }

    public void setY(int y) {
        this.y = y;
    }
}