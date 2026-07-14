package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogoutTests {
    @Test
    public void successfulUserLogout() {
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        AuthData auth = dataAccess.createUser(user);
        UserService userService = new UserService(dataAccess);
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
        userService.logoutUser(auth.authToken());
        Assertions.assertNull(dataAccess.getAuth(auth.authToken()));
    }
    @Test
    public void alreadyLoggedOut(){
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        AuthData auth = dataAccess.createUser(user);
        UserService userService = new UserService(dataAccess);
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
        userService.logoutUser(auth.authToken());


        // cannot log out with already invalid auth

        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logoutUser(auth.authToken()));
    }
}
