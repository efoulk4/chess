package client;

import chess.ChessGame;
import exceptions.ResponseException;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl implements ServerMessageHandler {
    private final ServerFacade facade;
    private final Session session = new Session();
    private final PreLoginClient preLogin;
    private final PostLoginClient postLogin;
    private final GameClient gameClient;
    Scanner scanner = new Scanner(System.in);

    public Repl(String serverUrl) {
        this.facade = new ServerFacade(serverUrl);
        this.preLogin = new PreLoginClient(facade, session);
        this.postLogin = new PostLoginClient(facade, session, this);
        this.gameClient = new GameClient(session, scanner);
        session.setServerURL(serverUrl);
    }

    public void run() {
        System.out.println(" Welcome to Chess. Sign in to start.");
        System.out.print(preLogin.help());

        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
            } catch (Throwable e) {
                var msg = e.getMessage();
                if (msg == null || msg.isBlank()) {
                    msg = "Error: something went wrong";
                }
                System.out.print(SET_TEXT_COLOR_RED + msg + RESET);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_BLUE);
    }

    public String eval(String input) throws ResponseException {
        return switch (session.getState()) {
            case PRELOGIN -> preLogin.eval(input);
            case POSTLOGIN -> postLogin.eval(input);
            case GAME -> gameClient.eval(input);
        };
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.print(RESET + "\n");
        switch (serverMessage.getServerMessageType()){
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
                handleLoadGame(loadGameMessage);}
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
                handleNotification(notificationMessage);}
            case ERROR -> {
                ErrorMessage errorMessage = (ErrorMessage) serverMessage;
                handleError(errorMessage);}
            }
            printPrompt();
        }

    private void handleLoadGame(LoadGameMessage loadGameMessage){
        ChessGame game =  loadGameMessage.getChessGame();
        session.setGame(game);
        System.out.print(BoardRenderer.draw(session.getGame().getBoard(), session.color(), null, null));
    }

    private void handleNotification(NotificationMessage notificationMessage){
            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + notificationMessage.getMessage() + RESET);
    }

    private void handleError(ErrorMessage errorMessage){
        System.out.print(SET_TEXT_COLOR_RED + errorMessage.getErrorMessage() + RESET);
    }
}


