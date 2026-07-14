package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClearTest {
    @Test
    public void clearDeletesAllFields() throws Exception {
        DataAccess dataAccess = new MemoryDataAccess();
        dataAccess.createAuth(new AuthData("myToken", "pitbull"));
        dataAccess.createGame("mySuperCoolGame");
        dataAccess.createUser(new UserData("mrWorldwide", "pw", "pmoney@gmail.com"));

        ClearService clearService = new ClearService(dataAccess);
         clearService.clear();

        Assertions.assertNull(dataAccess.getAuth("myToken"));
        Assertions.assertNull(dataAccess.getUser("mrWorldwide"));
        Assertions.assertNull(dataAccess.getGame(1));
    }
}
