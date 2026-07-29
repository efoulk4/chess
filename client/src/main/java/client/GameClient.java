package client;

import chess.ChessGame;

import java.util.Arrays;

public class GameClient {
    private final ServerFacade facade;
    private final Session session;
    public GameClient(ServerFacade facade, Session session) {
        this.facade = facade;
        this.session = session;
    }

    public String eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> BoardRenderer.draw(new ChessGame().getBoard(), session.color());
            case "leave" -> leave();
            case "quit" -> "quit";
            default -> help();
        };
    }
    private String leave() {
        session.setState(State.POSTLOGIN);
        session.setGameID(null);
        session.setColor(null);
        return "Left the game.";
    }

    public String help() {
        return """
                  - redraw - the board
                  - leave - the game
                  - help - with possible commands
                  """;
    }
}
