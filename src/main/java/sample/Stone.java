package sample;


import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
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

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.web("#705634"));

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setInput(dropShadow);
        innerShadow.setOffsetX(3.0f);
        innerShadow.setOffsetY(3.0f);

        if(color == ColorEnum.BLACK) {
            setFill(Color.web("#1b1b1b"));
            innerShadow.setColor(Color.web("#000000"));
            setEffect(innerShadow);
        }
        else if(color == ColorEnum.WHITE) {
            setFill(Color.WHITE);
            innerShadow.setColor(Color.web("#5f5f5f"));
            setEffect(innerShadow);
        }
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