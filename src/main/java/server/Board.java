package server;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int size;
    private Field fields[][];

    public Board(int size){
        this.size = size;
        fields = new Field[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                fields[i][j] = new Field(i,j,ColorEnum.EMPTY);
            }
        }
    }

    public boolean isInsideBoard(int x, int y){
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    public boolean isEmpty(int x, int y){
        if (!isInsideBoard(x,y))
            return false;
        else
            return getField(x,y).getColor().equals(ColorEnum.EMPTY);
    }


    public Field getField(int x, int y) {
        if(isInsideBoard(x,y))
            return fields[x][y];
        return null;
    }

    public boolean isMoveLegal(int x, int y){
        if(!isEmpty(x,y))
            return false;

        return true;
    }

    List<Field> getGroups(){
        ArrayList<Field> result = new ArrayList<Field>();
        int map[][] = new int[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = 0;
            }
        }



        return null;
    }

//    void mark(int x, int y, int map[][]){
//        Field field = getField(x,y);
//        if(field == null)
//            return;
//        map[x][y] = ;
//
//    }
}
