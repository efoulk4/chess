package server.websocket;
import io.javalin.websocket.WsContext;

public record Connection(String username, WsContext session) {
}
