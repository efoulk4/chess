package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;

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

    }

    private void onClose(WsCloseContext ctx) {

    }

    private void onError(WsErrorContext ctx) {

    }
}
