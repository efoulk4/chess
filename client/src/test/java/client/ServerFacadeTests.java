package client;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.AlreadyTakenException;
import service.CreateGameRequest;
import service.LoginRequest;
import service.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static UserData user;
    DataAccess dataAccess;

    @BeforeAll
    public static void init() {
        DataAccess dataAccess = new MySqlDataAccess();
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+ port);
        user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clear() {
        var authData = facade.register(user);
        var game1 = facade.createGame(new CreateGameRequest("mygame"));
        facade.clear();
        assertNull(dataAccess.listGames());
        assertNull(dataAccess.getAuth(authData.authToken()));
    }

    @Test
    public void createTestPositive()  {
        var authData = facade.register(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void createTestNegative() {
        var authData = facade.register(user);
        assertThrows(AlreadyTakenException.class, () -> facade.register(user));
    }

    @Test
    public void loginTestPositive() {
        var authData = facade.register(user);
        facade.logout(authData.authToken());
        assertThrows(DataAccessException.class, () -> facade.getGames(authData.authToken()));
        var newAuth = facade.login(new LoginRequest(user.username(), user.password()));
        assertDoesNotThrow(() ->facade.getGames(newAuth.authToken()));
    }

    @Test
    public void loginTestNegative() {
        var authData = facade.register(user);
        facade.logout(authData.authToken());
        assertThrows(DataAccessException.class, () -> facade.getGames(authData.authToken()));
        assertThrows(UnauthorizedException.class, () ->
                facade.login(new LoginRequest(user.username(), "abc123")));
    }

    @Test
    public void logoutTestPositive() {
        var authData = facade.register(user);
        facade.logout(authData.authToken());
        assertThrows(DataAccessException.class, () -> facade.getGames(authData.authToken()));
    }

    @Test
    public void logoutTestNegative() {
        var authData = facade.register(user);
        facade.logout(authData.authToken());
        assertThrows(DataAccessException.class, () -> facade.logout(authData.authToken()));
    }

    @Test
    public void getGamesTestPositive() {
        assertTrue(true);
    }

    @Test
    public void getGamesTestNegative() {
        assertTrue(true);
    }

    @Test
    public void createGameTestPositive() {
        assertTrue(true);
    }

    @Test
    public void createGameTestNegative() {
        assertTrue(true);
    }

    @Test
    public void joiGameTestPositive() {
        assertTrue(true);
    }

    @Test
    public void joinGameTestNegative() {
        assertTrue(true);
    }


}
