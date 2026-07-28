package client;

public class PostLoginClient {
    private final ServerFacade facade;
    private final Session session;
    public PostLoginClient(ServerFacade facade, Session session) {
        this.facade = facade;
        this.session = session;
    }
}
