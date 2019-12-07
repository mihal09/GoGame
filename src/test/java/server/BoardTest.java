package server;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @org.junit.Test
    public void isMoveLegal() {
        Board board = new Board(4);
        board.isMoveLegal(0,0,ColorEnum.BLACK);
        board.isMoveLegal(2,0,ColorEnum.BLACK);
        board.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(board.getGroups().size(),3);
        board.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(board.getGroups().size(),1);

        board = new Board(4);
        board.isMoveLegal(0,1,ColorEnum.BLACK);
        board.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(board.getGroups().size(),2);
        board.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(board.getGroups().size(),1);
        board.isMoveLegal(0,0,ColorEnum.BLACK);
        Assert.assertEquals(board.getGroups().size(),1);

    }

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
    public void isEmpty() {
    }

    @Test
    public void isSameColor() {
    }

    @Test
    public void getField() {
    }

    @Test
    public void findGroupWithStone() {
    }
}