package client.board;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void addStones() {
        Board board = new Board(4);
        board.addStones();
        Assert.assertNotNull(board.getStone(3,3));
    }
}