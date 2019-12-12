package server;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @org.junit.Test
    public void isMoveLegal() {

        Controller controller = new Controller(4);
        controller.isMoveLegal(0,0,ColorEnum.BLACK);
        controller.isMoveLegal(2,0,ColorEnum.BLACK);
        controller.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(controller.board.getGroups().size(),3);
        controller.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(controller.board.getGroups().size(),1);

        controller = new Controller(4);
        controller.isMoveLegal(0,1,ColorEnum.BLACK);
        controller.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(controller.board.getGroups().size(),2);
        controller.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(controller.board.getGroups().size(),1);
        controller.isMoveLegal(0,0,ColorEnum.BLACK);
        Assert.assertEquals(controller.board.getGroups().size(),1);

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
        Board board = new Board(4);

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