package client;

public class Repl {
    private final ServerFacade facade;
    private State state;
    private final Session session = new Session();
    private final PreLoginClient preLogin;
    private final PostLoginClient postLogin;
    private final GameClient gameClient;

    public Repl(String serverUrl) {
        this.facade = new ServerFacade(serverUrl);
        this.state = State.PRELOGIN;
        // hand the SAME facade and SAME session to each client:
        this.preLogin  = new PreLoginClient(facade, session);
        this.postLogin = new PostLoginClient(facade, session);
        this.gameClient = new GameClient(facade, session);
    }


}

