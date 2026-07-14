package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;

public class BaseServiceTests {
    protected DataAccess dataAccess;
    protected AuthService authService;
    protected GameService gameService;
    protected UserService userService;
    protected UserData pitbull;

@BeforeEach
   void initPitbull() throws Exception {
       dataAccess = new MemoryDataAccess();
       authService = new AuthService(dataAccess);
       gameService = new GameService(dataAccess, authService);
       userService = new UserService(dataAccess, authService);
       pitbull =  new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
   }}
