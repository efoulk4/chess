package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetGameTests extends BaseSqlTests{

    @Test
    public void retrieveValidGame(){
        GameData game = dataAccess.createGame("game1");
        Assertions.assertEquals("game1",dataAccess.getGame(game.gameID()).gameName());
    }

    @Test
    public void noSuchGame(){
        Assertions.assertNull(dataAccess.getGame(1));
    }
}
