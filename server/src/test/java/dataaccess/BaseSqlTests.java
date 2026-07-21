package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;

public class BaseSqlTests {
    protected  DataAccess dataAccess;
    protected UserData pitbull;

    @BeforeEach
    void initPitbull() throws Exception {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
        pitbull =  new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
    }
}
