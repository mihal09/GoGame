package server;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoneGroupTest {

    @Test
    public void hasBreath() {
        Board board = new Board(10);
        StoneGroup group = new StoneGroup(board, ColorEnum.WHITE);
        board.getField(1,0).setColor(ColorEnum.BLACK);
        board.getField(1,1).setColor(ColorEnum.BLACK);

        board.getField(0,0).setColor(ColorEnum.WHITE);
        group.addStone(board.getField(0,0));

        Assert.assertEquals(group.hasBreath(), true);
        board.getField(0,1).setColor(ColorEnum.BLACK);
        Assert.assertEquals(group.hasBreath(), false);
        board.getField(0,1).setColor(ColorEnum.WHITE);
        group.addStone(board.getField(0,1));
        Assert.assertEquals(group.hasBreath(), true);
        board.getField(0,2).setColor(ColorEnum.BLACK);
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
}