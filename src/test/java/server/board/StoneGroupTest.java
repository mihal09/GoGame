package server.board;

import org.junit.Assert;
import org.junit.Test;
import server.LogicController;
import server.enums.ColorEnum;

import java.awt.*;

import static org.junit.Assert.*;

public class StoneGroupTest {

    @Test
    public void hasBreath() {
        int size =4;
        LogicController logicController = new LogicController(size);
        Board board = logicController.getBoard();
        StoneGroup group = new StoneGroup(logicController.getBoard(), ColorEnum.BLACK);
        group.addStone(board.getField(0,0));
        Assert.assertEquals(group.hasBreath(), true);
        board.getField(0,1).setColor(ColorEnum.WHITE);
        board.getField(1,0).setColor(ColorEnum.WHITE);
        Assert.assertEquals(group.hasBreath(), false);
    }

    @Test
    public void containsStone() {
        Board board = new Board(10);
        StoneGroup group = new StoneGroup(board, ColorEnum.WHITE);
        Field stone = new Field(0,0, ColorEnum.WHITE);
        Assert.assertEquals(group.containsStone(stone), false);
        group.addStone(stone);
        Assert.assertEquals(group.containsStone(stone), true);
    }

    @Test
    public void getSize() {
    }

    @Test
    public void addStone() {
    }

    @Test
    public void getStones() {
    }

    @Test
    public void getColor() {
    }

    @Test
    public void getBoard() {
    }

}