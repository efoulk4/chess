package client;

import model.AuthData;
import model.UserData;
import model.LoginRequest;

import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade facade;
    private final Session session;

    public PreLoginClient(ServerFacade facade, Session session) {
        this.facade = facade;
        this.session = session;
    }

    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws RuntimeException {
        if (params.length == 3) {
            String username = params[0];
            String pw = params[1];
            String email = params[2];
            UserData user = new UserData(username, pw, email);
            AuthData auth = facade.register(user);
            session.setState(State.POSTLOGIN);
            session.setAuthToken(auth.authToken());
            return String.format("Signed in as %s.", auth.username());
        } else {
            throw new RuntimeException("Expected: register <USERNAME> <PASSWORD> <EMAIL>");
        }
    }
    public String login(String... params) throws RuntimeException {
        if (params.length == 2) {
            String username = params[0];
            String pw = params[1];
            LoginRequest loginRequest = new LoginRequest(username, pw);
            AuthData auth = facade.login(loginRequest);
            session.setState(State.POSTLOGIN);
            session.setAuthToken(auth.authToken());
            return String.format("Signed in as %s.", auth.username());
        } else {
            throw new RuntimeException("Expected: signIn <USERNAME> <PASSWORD>");
        }
    }

    public String help() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess
                - help - with possible commands
                """;
    }
}
