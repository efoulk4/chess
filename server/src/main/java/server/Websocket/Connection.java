package server.Websocket;
import io.javalin.websocket.WsContext;

public record Connection(String username, WsContext session) {
}
