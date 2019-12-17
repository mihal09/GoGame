package server.board;

import org.junit.Assert;
import org.junit.Test;
import server.LogicController;
import server.enums.ColorEnum;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest {



    @Test
    public void isInsideBoard() {
        int size =4;
        Board board = new Board(size);
        Assert.assertEquals(board.isInsideBoard(0,0), true);
        Assert.assertEquals(board.isInsideBoard(3,3), true);
        Assert.assertEquals(board.isInsideBoard(-1,0), false);
        Assert.assertEquals(board.isInsideBoard(0,4), false);
        Assert.assertEquals(board.isInsideBoard(4,4), false);
    }

    @Test
    public void setGroups() {
        int size =4;
        Board board = new Board(size);
        ArrayList<StoneGroup> groups;
        groups = new ArrayList<>();
        board.setGroups(groups);
        Assert.assertNotNull(board.getGroups());
    }

    @Test
    public void removeTerritory() {
        int size =4;
        Board board = new Board(size);
        board.getField(0,0).setColor(ColorEnum.EMPTY_BLACK);
        board.removeTerritory();
        Assert.assertEquals(ColorEnum.EMPTY, board.getField(0,0).getColor());
    }

    @Test
    public void getSize() {
        int size =4;
        Board board = new Board(size);
        Assert.assertEquals(size, board.getSize());
    }



    @Test
    public void isEmpty() {
    }

    @Test
    public void getStone() {
    }

    @Test
    public void isSameColor() {
    }

    @Test
    public void getField() {
    }

    @Test
    public void getGroups() {
    }

    @Test
    public void removeGroup() {
    }
}