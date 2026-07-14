package service;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateGameTests extends BaseServiceTests{
    @Test
    public void successfulCreateGame(){
        AuthData auth = dataAccess.createUser(pitbull);
        CreateGameResult result = gameService.createGame(auth.authToken(), new CreateGameRequest("game1"));
        int id = result.gameID();
        Assertions.assertNotNull(dataAccess.getGame(id));
    }

    @Test
    public void badCreateGameRequest(){
        AuthData auth = dataAccess.createUser(pitbull);
        Assertions.assertThrows(BadRequestException.class,
                () -> gameService.createGame(null, new CreateGameRequest("game1")));
        dataAccess.deleteAuth(auth.authToken());
        Assertions.assertThrows(UnauthorizedException.class,
                () -> gameService.createGame(auth.authToken(), new CreateGameRequest("game1")));
    }
}
