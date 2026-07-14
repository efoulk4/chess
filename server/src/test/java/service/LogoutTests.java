package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogoutTests extends BaseServiceTests {
    @Test
    public void successfulUserLogout() {
        AuthData auth = dataAccess.createUser(pitbull);
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
        userService.logoutUser(auth.authToken());
        Assertions.assertNull(dataAccess.getAuth(auth.authToken()));
    }
    @Test
    public void alreadyLoggedOut(){
        AuthData auth = dataAccess.createUser(pitbull);
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
        userService.logoutUser(auth.authToken());


        // cannot log out with already invalid auth

        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logoutUser(auth.authToken()));
    }
}
