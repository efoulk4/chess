package client;

public class PreLoginClient {
    private final ServerFacade facade;
    private final Session session;
    public PreLoginClient(ServerFacade facade, Session session) {
        this.facade = facade;
        this.session = session;
    }
}
