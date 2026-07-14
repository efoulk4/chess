package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class GameService {
    DataAccess dataAccess;
    AuthService authService;
    public GameService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }

    public Collection<GameData> getGames (String authToken) throws DataAccessException {
            authService.checkAuth(authToken);
            return dataAccess.listGames();
    }
    public CreateGameResult createGame(String authToken, CreateGameRequest req) throws DataAccessException{
        if(authToken == null || req == null || req.gameName() == null){
            throw new BadRequestException("Error: bad request");
        }
        authService.checkAuth(authToken);
        GameData game = dataAccess.createGame(req.gameName());
        return new CreateGameResult(game.gameID());
    }
    public void joinGame(String authToken, JoinGameRequest req){
        if(authToken == null || req == null || req.playerColor() == null || dataAccess.getGame(req.gameID()) == null) {
            throw new BadRequestException("Error: bad request");
        }
        authService.checkAuth(authToken);
        AuthData auth = dataAccess.getAuth(authToken);
        UserData user = dataAccess.getUser(auth.username());
        GameData game = dataAccess.getGame(req.gameID());
        GameData newgame = null;
        if (req.playerColor() == ChessGame.TeamColor.WHITE){
            if (game.whiteUsername() != null){
                throw new AlreadyTakenException("Error: already taken");
            }
            newgame = new GameData(game.gameID(),user.username(), game.blackUsername(),
                                            game.gameName(),game.game());
        }
        else if (req.playerColor() == ChessGame.TeamColor.BLACK){
            if (game.blackUsername() != null){
                throw new AlreadyTakenException("Error: already taken");
            }
            newgame = new GameData(game.gameID(),game.whiteUsername(), user.username(),
                    game.gameName(),game.game());
        }
        dataAccess.updateGame(newgame);
    }

}
