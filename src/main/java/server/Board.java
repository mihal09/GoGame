package server;

import java.util.ArrayList;

public class Board {
    private int size;
    private Field fields[][];
    private ArrayList<StoneGroup> groups;

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

    public boolean isMoveLegal(int x, int y, ColorEnum color){
        if(!isEmpty(x,y))
            return false;


        getField(x,y).setColor(color);

        ArrayList<StoneGroup> groupsCopy = new ArrayList<StoneGroup>(groups);


        StoneGroup newGroup = new StoneGroup(this, color);

        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if((i==0 && j == 0) || (i!=0 && j!=0))
                    continue;
                int newX = x+i;
                int newY = y+j;

                if(isSameColor(newX,newY, color)){
                    StoneGroup group = findGroupWithStone(groupsCopy, getField(newX,newY));
                    for(Field stone : group.getStones()){
                        newGroup.addStone(stone);
                    }
                    groupsCopy.remove(group);
                }
            }
        }

        groupsCopy.add(newGroup);

        groups = groupsCopy;


        return true;
    }

    public StoneGroup findGroupWithStone(ArrayList<StoneGroup> groupsList, Field stone){
        for( StoneGroup stoneGroup : groupsList){
            if(stoneGroup.containsStone(stone))
                return stoneGroup;
        }
        return null;
    }


    ArrayList<StoneGroup> getGroups(){
        int map[][] = new int[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = 0;
            }
        }

        return null;
    }
}
