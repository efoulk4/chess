package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class ListGamesTests extends BaseSqlTests{
    @Test
    public void returnsAllGames(){
       GameData game1 = dataAccess.createGame("game1");
       GameData game2 = dataAccess.createGame("game2");
       GameData game3 = dataAccess.createGame("game3");

        Collection<GameData> games = dataAccess.listGames();

        Assertions.assertEquals(3, games.size());
    }

    @Test
    public void emptyList(){
        Assertions.assertTrue(dataAccess.listGames().isEmpty());
    }
}
