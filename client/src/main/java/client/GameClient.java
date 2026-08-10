package client;

import chess.ChessGame;
import exceptions.ResponseException;
import websocket.commands.UserGameCommand;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class GameClient {
    private final ServerFacade facade;
    private final Session session;
    private final Scanner scanner;
    public GameClient(ServerFacade facade, Session session, Scanner scanner) {
        this.facade = facade;
        this.session = session;
        this.scanner = scanner;
    }

    public String eval(String input) throws ResponseException {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> BoardRenderer.draw(session.getGame().getBoard(), session.color());
            case "resign" -> resign();
            case "leave" -> leave();
            case "quit" -> "quit";
            default -> help();
        };
    }
    private String resign() throws ResponseException {

            String result = "";
            System.out.print("Are you sure you want to resign? <y/n> \n");
            result = scanner.nextLine();
            result = result.toLowerCase();
            if (Objects.equals(result, "y")||Objects.equals(result, "yes")){
                session.getFacade().send(
                new UserGameCommand(UserGameCommand.CommandType.RESIGN, session.authToken(), session.gameID()));
                return "Game resigned";
            }
            else {
                return "Resignation cancelled";
            }

    }
    private String leave() {
        session.setState(State.POSTLOGIN);
        session.setGameID(null);
        session.setColor(null);
        return "Left the game.";
    }

    public String help() {
        return """
                  - move <CHESS COORDINATE> <CHESS COORDINATE> - a piece
                  - resign - the game
                  - highlight <CHESS COORDINATE> - the legal moves for that piece
                  - redraw - the board
                  - leave - the game
                  - help - with possible commands
                  """;
    }
}
