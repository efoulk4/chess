package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class CreateUserTests extends BaseSqlTests{

    @Test
    public void successfullyAddUser(){
        AuthData auth = dataAccess.createUser(pitbull);
        Assertions.assertEquals(auth.username(), "mrWorldwide");
    }

    @Test
    public void cannotAddSameUser(){
        dataAccess.createUser(pitbull);
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createUser(pitbull));
    }
}
