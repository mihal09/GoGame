package server;

import org.junit.Assert;
import org.junit.Test;
import server.enums.ColorEnum;

import static org.junit.Assert.*;

public class LogicControllerTest {

    @org.junit.Test
    public void isMoveLegal() {

        LogicController logicController = new LogicController(7);
        logicController.isMoveLegal(0,0, ColorEnum.BLACK);
        logicController.isMoveLegal(6,5, ColorEnum.WHITE);
        logicController.isMoveLegal(2,0,ColorEnum.BLACK);
        logicController.isMoveLegal(6,6, ColorEnum.WHITE);
        logicController.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),4);
        logicController.isMoveLegal(6,4, ColorEnum.WHITE);
        logicController.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),2);

        logicController = new LogicController(7);
        logicController.isMoveLegal(0,1,ColorEnum.BLACK);
        logicController.isMoveLegal(6,6, ColorEnum.WHITE);
        logicController.isMoveLegal(1,0,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),3);
        logicController.isMoveLegal(6,5, ColorEnum.WHITE);
        logicController.isMoveLegal(1,1,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),2);
        logicController.isMoveLegal(6,4, ColorEnum.WHITE);
        logicController.isMoveLegal(0,0,ColorEnum.BLACK);
        Assert.assertEquals(logicController.getBoard().getGroups().size(),2);

    }

    @Test
    public void findDeadGroups() {
        LogicController logicController = new LogicController(7);
        logicController.isMoveLegal(0,0, ColorEnum.BLACK);
        logicController.isMoveLegal(0,1, ColorEnum.WHITE);
        logicController.isMoveLegal(5,5, ColorEnum.BLACK);
        Assert.assertEquals(logicController.findDeadGroups(logicController.getBoard().getGroups(), ColorEnum.BLACK).size(),0);
        logicController.getBoard().getField(1,0).setColor(ColorEnum.WHITE);
        Assert.assertEquals(logicController.findDeadGroups(logicController.getBoard().getGroups(), ColorEnum.BLACK).size(),1);
    }

    @Test
    public void getScore() {
        LogicController logicController = new LogicController(7);
        logicController.isMoveLegal(0,0, ColorEnum.BLACK);
        logicController.isMoveLegal(0,1, ColorEnum.WHITE);
        logicController.isMoveLegal(5,5, ColorEnum.BLACK);
        logicController.isMoveLegal(1,0, ColorEnum.WHITE);
        int [] scores = logicController.getScore();
        Assert.assertEquals(scores[1], 1);

    }
}