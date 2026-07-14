package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.UUID;

public interface DataAccess {
    void clear () throws DataAccessException;
    AuthData createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    default String generateToken() {
        return UUID.randomUUID().toString();
    }
}
