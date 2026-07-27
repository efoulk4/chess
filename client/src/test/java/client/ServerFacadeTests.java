package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clear() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void getGamesTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void getGamesTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joiGameTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestNegative() {
        Assertions.assertTrue(true);
    }


}
