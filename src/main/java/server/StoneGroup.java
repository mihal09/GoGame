package server;

import java.util.ArrayList;

public class StoneGroup {
    private ColorEnum color;
    private Board board;

    private ArrayList<Field> stones;

    public StoneGroup(Board board, ColorEnum color){
        stones = new ArrayList<Field>();
        this.color = color;
        this.board = board;
    }

    public void addStone(Field stone){
        stones.add(stone);
    }

    public boolean hasBreath(){
        for (Field stone : stones)
        {
            int x = stone.getX();
            int y = stone.getY();
            if(board.isEmpty(x+1,y)) return true;
            if(board.isEmpty(x-1,y)) return true;
            if(board.isEmpty(x,y-1)) return true;
            if(board.isEmpty(x,y+1)) return true;
        }
        return false;
    }

    public ArrayList<Field> getStones(){
        return stones;
    }
}
