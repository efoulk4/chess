package client;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.*;

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
        var game1 = facade.createGame(new CreateGameRequest("mygame"), authData.authToken());
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
        var authData = facade.register(user);
        facade.createGame(new CreateGameRequest("mygame"), authData.authToken());
        facade.createGame(new CreateGameRequest("game2"), authData.authToken());
        var games = facade.getGames(authData.authToken());
        assertTrue(games.size() >1);
    }

    @Test
    public void getGamesTestNegative() {
        var authData = facade.register(user);
        facade.createGame(new CreateGameRequest("mygame"), authData.authToken());
        facade.createGame(new CreateGameRequest("game2"), authData.authToken());
        assertThrows(UnauthorizedException.class, () -> facade.getGames("myAuth"));
    }

    @Test
    public void createGameTestPositive() {
        var authData = facade.register(user);
        CreateGameResult cgr = facade.createGame(new CreateGameRequest("game1"), authData.authToken());
        int gameID = cgr.gameID();
        assertNotNull(dataAccess.getGame(gameID));
    }

    @Test
    public void createGameTestNegative() {
        assertThrows(UnauthorizedException.class, () ->
                facade.createGame(new CreateGameRequest("game1"), "trashAuth"));
    }

    @Test
    public void joiGameTestPositive() {
        var authData = facade.register(user);
        UserData user2 = new UserData("james","pw2", "j@j.j");
        var authData2 = facade.register(user2);
        CreateGameResult cgr = facade.createGame(new CreateGameRequest("game1"), authData.authToken());
        int gameID = cgr.gameID();
        JoinGameRequest joinGameRequestWhite = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        JoinGameRequest joinGameRequestBlack = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        facade.joinGame(joinGameRequestWhite, authData.authToken());
        facade.joinGame(joinGameRequestBlack, authData2.authToken());
        assertNotNull(dataAccess.getGame(gameID).blackUsername());
        assertNotNull(dataAccess.getGame(gameID).whiteUsername());
    }

    @Test
    public void joinGameTestNegative() {
        var authData = facade.register(user);
        UserData user2 = new UserData("james","pw2", "j@j.j");
        var authData2 = facade.register(user2);
        CreateGameResult cgr = facade.createGame(new CreateGameRequest("game1"), authData.authToken());
        int gameID = cgr.gameID();
        JoinGameRequest joinGameRequestWhite = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        JoinGameRequest joinGameRequestBlack = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        facade.joinGame(joinGameRequestWhite, authData.authToken());
        assertThrows(AlreadyTakenException.class, () ->
                facade.joinGame(joinGameRequestBlack, authData2.authToken()));
    }


}
