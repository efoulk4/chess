package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.GameData;
import model.UserData;
import service.*;

import java.util.Collection;
import java.util.Map;

public class Server {
    private final DataAccess dataAccess;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;
    private final Gson gson;

    private final Javalin javalin;

    public Server() {
        dataAccess = new MemoryDataAccess();
        authService = new AuthService(dataAccess);
        clearService = new ClearService(dataAccess);
        userService = new UserService(dataAccess, authService);
        gameService = new GameService(dataAccess, authService);
        gson = new Gson();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearData)
                .post("/user", this::addUser)
                .post("/session", this::loginUser)
                .delete("/session", this::logoutUser)
                .get("/game", this::listGames)
                .post("/game", this::createGame)



                .exception(BadRequestException.class, (e, ctx)  ->
                        ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(UnauthorizedException.class, (e, ctx)  ->
                        ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(AlreadyTakenException.class, (e, ctx)  ->
                        ctx.status(403).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(DataAccessException.class, (e, ctx)  ->
                        ctx.status(500).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(Exception.class, (e, ctx) ->
                        ctx.status(500).result(gson.toJson(Map.of("message", "Error: " + e.getMessage()))));
    }
    public void clearData (Context ctx){
        clearService.clear();
        ctx.result("{}");
    }
    public void addUser (Context ctx){
        UserData req = gson.fromJson(ctx.body(), UserData.class);
        ctx.result(gson.toJson(userService.registerUser(req)));
    }
    public void loginUser (Context ctx){
        LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);
        ctx.result(gson.toJson(userService.loginUser(req)));
    }
    public void logoutUser (Context ctx) {
        String authToken = ctx.header("authorization");
        userService.logoutUser(authToken);
        ctx.result("{}");
    }
    public void listGames (Context ctx){
        String authToken = ctx.header("authorization");
        Collection<GameData> res = gameService.getGames(authToken);
        ListGamesResult list = new ListGamesResult(res);
        ctx.result(gson.toJson(list));
    }
    public void createGame (Context ctx) {
        String authToken = ctx.header("authorization");
        CreateGameRequest req = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult res =  gameService.createGame(authToken, req);
        ctx.result(gson.toJson(res));
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
