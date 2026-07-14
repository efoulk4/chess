package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests {
    @Test
    public void successfulUserLogin() {
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        dataAccess.createUser(user);
        UserService userService = new UserService(dataAccess);
        LoginRequest login = new LoginRequest(user.username(), user.password());
        Assertions.assertInstanceOf(AuthData.class, userService.loginUser(login));
    }
    @Test
    public void badUsernameOrPassword(){
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        dataAccess.createUser(user);
        UserService userService = new UserService(dataAccess);

        LoginRequest badUser =  new LoginRequest("snoop", "pw");
        LoginRequest badPass =  new LoginRequest("mrWorldwide", "abc");


        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(badUser));
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(badPass));
    }
}