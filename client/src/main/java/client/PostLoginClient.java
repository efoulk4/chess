package client;

import java.util.Arrays;

public class PostLoginClient {
    private final ServerFacade facade;
    private final Session session;
    public PostLoginClient(ServerFacade facade, Session session) {
        this.facade = facade;
        this.session = session;
    }
    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }



    public String help() {
        return """
                    - create <GAMENAME> - a game
                    - list - games
                    - join <GAMEID> <WHITE|BLACK> - a game
                    - observe <GAMEID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """;
    }
}
