package client;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainControllerTest {

    private static MainController mainController;

    @BeforeClass
    public static void setUp() {
        mainController = new MainController(new Main());
    }

    @Test
    public void drawBoard() {
        mainController.createBoard(5);
        mainController.drawBoard();
    }
}