package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ListGamesTests {
    @Test
    public void successfulListGames (){
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        AuthData auth = dataAccess.createUser(user);
        dataAccess.createGame("game1");
        dataAccess.createGame("game2");
        AuthService authService = new AuthService(dataAccess);
        GameService gameService = new GameService(dataAccess, authService);
        Assertions.assertEquals(dataAccess.listGames(), gameService.getGames(auth.authToken()));
    }
    @Test
    public void unauthorizedCannotViewGames() {
        
    }
}
