package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

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

}
