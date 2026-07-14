package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ListGamesTests extends BaseServiceTests{
    @Test
    public void successfulListGames (){
        AuthData auth = dataAccess.createUser(pitbull);
        dataAccess.createGame("game1");
        dataAccess.createGame("game2");
        Assertions.assertEquals(dataAccess.listGames(), gameService.getGames(auth.authToken()));
    }
    @Test
    public void unauthorizedCannotViewGames() {
        AuthData auth = dataAccess.createUser(pitbull);
        dataAccess.createGame("game1");
        dataAccess.createGame("game2");
        userService.logoutUser(auth.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.getGames(auth.authToken()));
    }
}
