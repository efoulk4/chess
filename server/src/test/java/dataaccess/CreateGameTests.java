package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateGameTests extends BaseSqlTests{

    @Test
    public void successfullyMakeNewGame(){
        GameData game = dataAccess.createGame("game1");
        Assertions.assertNotNull(dataAccess.getGame(game.gameID()));
    }

    @Test
    public void mustProvideGameName(){
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createGame(null));
    }
}
