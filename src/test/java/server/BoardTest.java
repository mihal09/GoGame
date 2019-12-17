package server;

import org.junit.Assert;
import org.junit.Test;
import server.board.Board;
import server.enums.ColorEnum;

public class BoardTest {

    @org.junit.Test
    public void isMoveLegal() {

        LogicController logicController = new LogicController(4);
        logicController.isMoveLegal(0,0, ColorEnum.BLACK);
        logicController.isMoveLegal(2,0,ColorEnum.BLACK);
        logicController.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),3);
        logicController.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),1);

        logicController = new LogicController(4);
        logicController.isMoveLegal(0,1,ColorEnum.BLACK);
        logicController.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),2);
        logicController.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),1);
        logicController.isMoveLegal(0,0,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),1);

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