package service;

import dataaccess.DataAccess;
import model.AuthData;

public class AuthService {
    DataAccess dataAccess;
    public AuthService(DataAccess dataAccess) {this.dataAccess = dataAccess;}



    public AuthData checkAuth(String authToken){
        if (authToken == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthData auth = dataAccess.getAuth(authToken);
        if (auth == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth;
    }

}
