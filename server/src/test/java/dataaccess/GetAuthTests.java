package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetAuthTests extends BaseSqlTests{
    @Test
    public void thereIsAuth() {
        AuthData auth  = dataAccess.createUser(pitbull);
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
    }
    @Test
    public void thereIsNotAuth() {
        AuthData auth  = dataAccess.createUser(pitbull);
        Assertions.assertNull(dataAccess.getAuth("bingoBangoBongo"));
    }
}
