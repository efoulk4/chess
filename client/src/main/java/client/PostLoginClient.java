package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.JoinGameRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient {
    private final ServerFacade facade;
    private final Session session;
    private final Map<Integer, GameData> gameList = new HashMap<>();
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
            gameList.put(i, game);
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
    }
    public String join(String... params) throws RuntimeException {
        if (params.length == 2) {
            int frontendGameID = Integer.parseInt(params[0]);
            ChessGame.TeamColor color;
            try {
                color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Color must be WHITE or BLACK");
            }
            GameData game = gameList.get(frontendGameID);
            if (game == null) {
                throw new RuntimeException("No game " + frontendGameID + ". Type 'list' first.");
            }
            JoinGameRequest jgr = new JoinGameRequest(color, game.gameID());
            facade.joinGame(jgr, session.authToken());
            session.setGameID(game.gameID());
            session.setColor(color);
            session.setState(State.GAME);
            return BoardRenderer.draw(game.game().getBoard(), color);
        } else {
            throw new RuntimeException("Expected: join <GAMEID> <WHITE|BLACK>");
        }
    }
    public String observe(String... params) throws RuntimeException{
        if (params.length == 1) {
            Integer frontendGameID = Integer.parseInt(params[0]);
            GameData game = gameList.get(frontendGameID);
            if (game == null) {
                throw new RuntimeException("No game " + frontendGameID + ". Type 'list' first.");
            }
            session.setGameID(game.gameID());
            session.setState(State.GAME);
            session.setColor(null);
            return BoardRenderer.draw(game.game().getBoard(), ChessGame.TeamColor.WHITE);
        } else {
            throw new RuntimeException("Expected: observe <GAMEID>");
        }
    }
    public String logout() throws RuntimeException{
        facade.logout(session.authToken());
        session.setAuthToken(null);
        session.setState(State.PRELOGIN);
        return "Logged Out, Thanks for playing!";
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
