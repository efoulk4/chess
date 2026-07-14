package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTests {
    @Test
    public void successfulUserCreation() {
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        UserService userService = new UserService(dataAccess);
        AuthData auth = userService.registerUser(user);
        Assertions.assertEquals(user, dataAccess.getUser("mrWorldwide"));
        Assertions.assertNotNull(auth.authToken());
    }
    @Test
    public void cannotDuplicateNames(){
        DataAccess dataAccess = new MemoryDataAccess();
        UserData user = new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        UserService userService = new UserService(dataAccess);
        AuthData auth = userService.registerUser(user);

        //Second User Cannot register under the same username
        UserData user2 = new UserData("mrWorldwide", "pw2", "therealpmoney@gmail.com");
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.registerUser(user2));
    }
}
