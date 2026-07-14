package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests extends BaseServiceTests{
    @Test
    public void successfulUserLogin() {
        dataAccess.createUser(pitbull);
        LoginRequest login = new LoginRequest(pitbull.username(), pitbull.password());
        Assertions.assertInstanceOf(AuthData.class, userService.loginUser(login));
    }
    @Test
    public void badUsernameOrPassword(){
        dataAccess.createUser(pitbull);

        LoginRequest badUser =  new LoginRequest("snoop", "pw");
        LoginRequest badPass =  new LoginRequest("mrWorldwide", "abc");


        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(badUser));
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(badPass));
    }
}