package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class UpdateGameTests extends BaseSqlTests {

    @Test
    public void lotsOfChanges() throws InvalidMoveException {
        GameData game = dataAccess.createGame("mygame");
        ChessGame chessGame = game.game();
        chessGame.makeMove(new ChessMove(e2, e4, null));
        chessGame.makeMove(new ChessMove(d7, d5, null));
        dataAccess.updateGame(game);
        GameData sameGameData = dataAccess.getGame(game.gameID());
        ChessGame sameGame = sameGameData.game();
        sameGame.makeMove(new ChessMove(f1, b5, null));
        Assertions.assertTrue(sameGame.isInCheck(ChessGame.TeamColor.BLACK));
    }
    @Test
    public void invalidGameID () throws InvalidMoveException {
        GameData game = dataAccess.createGame("bingo");
        ChessGame chessGame = game.game();
        chessGame.makeMove(new ChessMove(e2, e4, null));
        dataAccess.updateGame(game);
        GameData fakeGame =
                new GameData(4, null, null, "bongo", new ChessGame());
        Collection<GameData> gamesBefore = dataAccess.listGames();
        dataAccess.updateGame(fakeGame);
        Assertions.assertEquals(gamesBefore, dataAccess.listGames());
    }
}
