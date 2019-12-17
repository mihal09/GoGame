package client;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;

import static org.junit.Assert.*;

public class ClientTest {

    private static Client client;

    @BeforeClass
    public static void setUp() {
        client = new Client();
    }

    @Test
    public void setController() {
        client.setController(new MainController(new Main()));
    }

    @Test(expected = ConnectException.class)
    public void setConnection() throws IOException {
        String address = "localhost";
        client.setConnection(address);
    }

    @Test(expected = NullPointerException.class)
    public void sendMessage() {
        client.sendMessage("message");
    }
}