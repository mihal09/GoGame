package server;

import org.junit.Assert;

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
}