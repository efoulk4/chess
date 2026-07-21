package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeleteAuthTests extends BaseSqlTests{

    @Test
    public void deleteThatAuth(){
        AuthData auth = dataAccess.createUser(pitbull);
        dataAccess.deleteAuth(auth.authToken());
        Assertions.assertNull(dataAccess.getAuth(auth.authToken()));
    }

    @Test
    public void dontDeleteThatAuthBecauseItIsInFactNotThere (){
        AuthData auth  = dataAccess.createUser(pitbull);
        Assertions.assertDoesNotThrow(() -> dataAccess.deleteAuth("bingo"));
        Assertions.assertNotNull(dataAccess.getAuth(auth.authToken()));
    }
}
