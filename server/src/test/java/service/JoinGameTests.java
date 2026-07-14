package service;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JoinGameTests extends BaseServiceTests{
    @Test
    public void successfulJoinGame() {
        AuthData auth = dataAccess.createUser(pitbull);
        GameData game =  dataAccess.createGame("game1");
        JoinGameRequest req = new JoinGameRequest(ChessGame.TeamColor.WHITE, game.gameID());
        gameService.joinGame(auth.authToken(),req);
        Assertions.assertEquals("mrWorldwide", dataAccess.getGame(game.gameID()).whiteUsername());
    }

    @Test
    public void allFailCases(){
        AuthData auth = dataAccess.createUser(pitbull);
        GameData game =  dataAccess.createGame("game1");
        JoinGameRequest req = new JoinGameRequest(ChessGame.TeamColor.WHITE, game.gameID());

        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(auth.authToken(),null));
        gameService.joinGame(auth.authToken(),req);
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(auth.authToken(),req));
        dataAccess.deleteAuth(auth.authToken());
        JoinGameRequest req2 = new JoinGameRequest(ChessGame.TeamColor.BLACK, game.gameID());
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.joinGame(auth.authToken(),req2));
    }
}
