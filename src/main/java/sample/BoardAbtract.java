package sample;

import javafx.scene.Group;

abstract class BoardAbstract extends Group {
    //size of the board
    int size;
    //widths of particular rows


    Field fields[][];
    Stone stones[][];

    public int getSize() {
        return size;
    }

    public Field getField(int x, int y) {
        return fields[x][y];
    }

    public Stone getStone(int x, int y) {
        return stones[x][y];
    }

    public boolean isEmpty(int x, int y){
        return getStone(x,y).getColor().equals(ColorEnum.EMPTY);
    }
}