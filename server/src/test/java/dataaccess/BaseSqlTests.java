package dataaccess;

import chess.ChessPosition;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;

public class BaseSqlTests {
    protected  DataAccess dataAccess;
    protected UserData pitbull;
    ChessPosition e2;
    ChessPosition e4;
    ChessPosition d7;
    ChessPosition d5;
    ChessPosition f1;
    ChessPosition b5;

    @BeforeEach
    void initPitbull() throws Exception {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
        pitbull =  new UserData("mrWorldwide", "pw", "pmoney@gmail.com");
        e2 = new ChessPosition(2,5);
        e4 = new ChessPosition(4,5);
        d7 = new ChessPosition(7,4);
        d5 = new ChessPosition(5,4);
        f1 = new ChessPosition(1,6);
        b5 = new ChessPosition(5,2);
    }
}
