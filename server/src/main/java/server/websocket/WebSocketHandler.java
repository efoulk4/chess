package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Objects;

public class WebSocketHandler {
    private final DataAccess dataAccess;
    private final ConnectionManager connections;
    private final Gson gson;

    public WebSocketHandler(DataAccess dataAccess, ConnectionManager connections) {
        this.dataAccess = dataAccess;
        this.connections = connections;
        this.gson = new Gson();
    }

    public void configure(WsConfig ws) {
        ws.onConnect(this::onConnect);
        ws.onMessage(this::onMessage);
        ws.onClose(this::onClose);
        ws.onError(this::onError);
    }

    private void onConnect(WsConnectContext ctx) {

    }

    private void onMessage(WsMessageContext ctx) {
        UserGameCommand base = gson.fromJson(ctx.message(), UserGameCommand.class);
        AuthData auth = dataAccess.getAuth(base.getAuthToken());
        if (auth == null){
            ctx.send((gson.toJson(new RuntimeException("Error: Invalid Auth"))));
            return;
        }
        String username = auth.username();

        switch (base.getCommandType()){
            case CONNECT -> {connect(ctx, base, username);}

        };


    }

    private void onClose(WsCloseContext ctx) {

    }

    private void onError(WsErrorContext ctx) {

    }

    private void connect(WsMessageContext ctx, UserGameCommand cmd, String username) {
        GameData gameData = dataAccess.getGame(cmd.getGameID());
        if (gameData == null){
            ctx.send(new RuntimeException("Error: No such game."));
            return;
        }
        connections.add(cmd.getGameID(), cmd.getAuthToken(), username, ctx);
        ctx.send(gson.toJson(new LoadGameMessage(gameData.game())));

        String role = "an observer";
        if (Objects.equals(gameData.whiteUsername(), username)){role = "white";}
        if (Objects.equals(gameData.blackUsername(), username)){role = "black";}
        String text = username + " joined as" + role;
        connections.broadcast(gameData.gameID(), cmd.getAuthToken(), gson.toJson(new NotificationMessage(text)));


    }
}
