package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetUserTests extends BaseSqlTests{

    @Test
    public void returnNewUser(){
        dataAccess.createUser(pitbull);
        Assertions.assertEquals(dataAccess.getUser("mrWorldwide").email(), pitbull.email());
    }

    @Test
    public void noSuchUser(){
        Assertions.assertNull(dataAccess.getUser("Johnny"));
    }
}
