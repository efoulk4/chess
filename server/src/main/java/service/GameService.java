package service;

import dataaccess.DataAccess;
import model.GameData;

import java.util.Collection;

public class GameService {
    DataAccess dataAccess;
    AuthService authService;
    public GameService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }

    public Collection<GameData> getGames (String authToken){
            authService.checkAuth(authToken);
            return dataAccess.listGames();
    }

}
