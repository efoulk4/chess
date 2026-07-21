package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTests extends BaseServiceTests {
    @Test
    public void successfulUserCreation() {
        AuthData auth = userService.registerUser(pitbull);
        Assertions.assertEquals(pitbull.username(), dataAccess.getUser("mrWorldwide").username());
        Assertions.assertNotNull(auth.authToken());
    }
    @Test
    public void cannotDuplicateNames(){
        AuthData auth = userService.registerUser(pitbull);

        //Second User Cannot register under the same username
        UserData user2 = new UserData("mrWorldwide", "pw2", "therealpmoney@gmail.com");
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.registerUser(user2));
    }
}
