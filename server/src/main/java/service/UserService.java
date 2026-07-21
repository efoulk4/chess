package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public class UserService {
    DataAccess dataAccess;
    AuthService authService;
    public UserService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }
        public AuthData registerUser(UserData user) throws DataAccessException {
            if (user.username() == null || user.password() == null || user.email() == null){
                throw new BadRequestException("Error: bad request");
            }
            if(dataAccess.getUser(user.username()) != null){
                throw new AlreadyTakenException("Error: already taken");
            }
            String hash = BCrypt.hashpw(user.password(),BCrypt.gensalt());
            UserData hashedUser = new UserData(user.username(), hash, user.email());
            return dataAccess.createUser(hashedUser);
            }


        public AuthData loginUser(LoginRequest user) throws DataAccessException{
            if (user.username() == null || user.password() == null){
                throw new BadRequestException("Error: bad request");
            }
            UserData userData = dataAccess.getUser(user.username());
            if(userData == null || !BCrypt.checkpw(user.password(), userData.password())){
                throw new UnauthorizedException("Error: unauthorized");
            }
            AuthData newAuth = new AuthData(dataAccess.generateToken(), user.username());
            dataAccess.createAuth(newAuth);
            return newAuth;
            }

        public void logoutUser(String authToken) throws DataAccessException {
        authService.checkAuth(authToken);
        dataAccess.deleteAuth(authToken);
        }
}
