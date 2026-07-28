package client;

public class GameClient {
    private final ServerFacade facade;
    private final Session session;
    public GameClient(ServerFacade facade, Session session) {
        this.facade = facade;
        this.session = session;
    }
}
