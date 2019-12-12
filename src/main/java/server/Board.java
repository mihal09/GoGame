package server;

import java.util.ArrayList;

public class Board {
    private int size;
    private Field fields[][];

    public void setGroups(ArrayList<StoneGroup> groups) {
        this.groups = groups;
    }

    private ArrayList<StoneGroup> groups;

    public Board(int size){
        this.size = size;
        this.groups = new ArrayList<StoneGroup>();
        fields = new Field[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                fields[i][j] = new Field(i,j,ColorEnum.EMPTY);
            }
        }
    }

//    public ArrayList<StoneGroup> makeGroupsCopy(){
//        ArrayList<StoneGroup> copy = new ArrayList<StoneGroup>();
//        for (StoneGroup oldGroup : groups) {
//            StoneGroup newGroup = new StoneGroup(oldGroup);
//            copy.add(newGroup);
//        }
//        return copy;
//    }

    public boolean isInsideBoard(int x, int y){
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    public boolean isEmpty(int x, int y){
        return isSameColor(x,y,ColorEnum.EMPTY);
    }

    public boolean isSameColor(int x, int y, ColorEnum checkedColor){
        if (!isInsideBoard(x,y))
            return false;
        else
            return getField(x,y).getColor().equals(checkedColor);
    }


    public Field getField(int x, int y) {
        if(isInsideBoard(x,y))
            return fields[x][y];
        return null;
    }

    ArrayList<StoneGroup> getGroups(){
        return groups;
    }

    public void removeGroup(StoneGroup group){
        groups.remove(group);
    }
}
