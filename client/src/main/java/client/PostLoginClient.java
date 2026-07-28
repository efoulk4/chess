package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import service.CreateGameRequest;
import service.CreateGameResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient {
    private final ServerFacade facade;
    private final Session session;
    private final Map<Integer, Integer> gameList = new HashMap<>();
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
    public String create(String... params) throws RuntimeException {
        if (params.length == 1) {
            String gameName = params[0];
            CreateGameRequest cgr = new CreateGameRequest(gameName);
            facade.createGame(cgr, session.authToken());
            return String.format("Created game: %s.", gameName);
        } else {
            throw new RuntimeException("Expected: create <GAMENAME> - a game");
        }
    }
    public String list() throws RuntimeException {
        Collection<GameData> games = facade.getGames(session.authToken());
        gameList.clear();
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (GameData game: games){
            gameList.put(i, game.gameID());
            String whiteName = game.whiteUsername();
            String blackName = game.blackUsername();
            sb.append(String.format("%d. %s: White: %s, Black: %s%n", i, game.gameName(), whiteName, blackName));
            i++;
        }
        if (gameList.isEmpty()){
            return "No games yet -- make one with create <GAMENAME>";
        }
        else{
            return sb.toString();
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
