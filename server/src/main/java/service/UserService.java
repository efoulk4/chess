package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class UserService {
    DataAccess dataAccess;
    public UserService(DataAccess dataAccess) {this.dataAccess = dataAccess;}
        public AuthData registerUser(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null){
            throw new BadRequestException("Error: bad request");
        }
        if(dataAccess.getUser(user.username()) != null){
            throw new AlreadyTakenException("Error: already taken");
        }
        return dataAccess.createUser(user);
        }
}
