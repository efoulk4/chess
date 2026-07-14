package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.*;

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
        clearService = new ClearService(dataAccess);
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
        authService = new AuthService(dataAccess);
        gson = new Gson();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearData)
                .post("/user", this::addUser)


                .exception(BadRequestException.class, (e, ctx)  ->
                        ctx.status(400).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(UnauthorizedException.class, (e, ctx)  ->
                        ctx.status(401).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(AlreadyTakenException.class, (e, ctx)  ->
                        ctx.status(403).result(gson.toJson(Map.of("message", e.getMessage()))))
                .exception(DataAccessException.class, (e, ctx)  ->
                        ctx.status(500).result(gson.toJson(Map.of("message", e.getMessage()))));
    }
    public void clearData (Context ctx){
        clearService.clear();
        ctx.result("{}");
    }
    public void addUser (Context ctx){
        UserData req = gson.fromJson(ctx.body(), UserData.class);
        ctx.result(gson.toJson(userService.registerUser(req)));
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
