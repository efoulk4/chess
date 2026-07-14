package server;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        DataAccess dataAccess = new MemoryDataAccess();
        ClearService clearService = new ClearService(dataAccess);
        UserService userService = new UserService(dataAccess);
        GameService gameService = new GameService(dataAccess);
        AuthService authService = new AuthService(dataAccess);



        javalin.exception(BadRequestException.class, (e, ctx)  ->
                ctx.status(400).json(e));
        javalin.exception(UnauthorizedException.class, (e, ctx)  ->
                ctx.status(401).json(e));
        javalin.exception(AlreadyTakenException.class, (e, ctx)  ->
                ctx.status(403).json(e));
        javalin.exception(DataAccessException.class, (e, ctx)  ->
                ctx.status(500).json(e));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
