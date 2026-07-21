package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateAuthTests extends BaseSqlTests{

    @Test
    public void validCreation(){
        AuthData auth = dataAccess.createUser(pitbull);
        dataAccess.deleteAuth(auth.authToken());
        dataAccess.createAuth(auth);
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
    }

    @Test
    public void theresOnlyOnePitbull(){
        AuthData auth = dataAccess.createUser(pitbull);
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createAuth(auth));
    }
}
