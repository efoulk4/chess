package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
    private final ServerFacade facade;
    private final Session session = new Session();
    private final PreLoginClient preLogin;
    private final PostLoginClient postLogin;
    private final GameClient gameClient;

    public Repl(String serverUrl) {
        this.facade = new ServerFacade(serverUrl);
        this.preLogin = new PreLoginClient(facade, session);
        this.postLogin = new PostLoginClient(facade, session);
        this.gameClient = new GameClient(facade, session);
    }

    public void run() {
        System.out.println(" Welcome to Chess. Sign in to start.");
        System.out.print(preLogin.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_BLUE);
    }

    public String eval(String input) {
        return switch (session.getState()) {
            case PRELOGIN -> preLogin.eval(input);
            case POSTLOGIN -> postLogin.eval(input);
            case GAME -> gameClient.eval(input);
        };
    }
}

