package dataaccess;

import org.junit.jupiter.api.Test;

public class ClearTest extends BaseSqlTests{

    @Test
    public void clearDeletesDB(){
        dataAccess.createUser(pitbull);
    }
}
