package client;

import com.google.gson.Gson;
import exceptions.ResponseException;
import jakarta.websocket.*;
import jakarta.websocket.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketFacade extends Endpoint {

    Session wsSession;
    Gson gson = new Gson();
    ServerMessageHandler serverMessageHandler;

    public WebsocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.wsSession = container.connectToServer(this, socketURI);

            this.wsSession.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    var type = serverMessage.getServerMessageType();

                    ServerMessage parsed = switch (type){
                        case NOTIFICATION -> gson.fromJson(message, NotificationMessage.class);
                        case LOAD_GAME -> gson.fromJson(message, LoadGameMessage.class);
                        case ERROR -> gson.fromJson(message, ErrorMessage.class);
                    };
                    serverMessageHandler.notify(parsed);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
    public void send(UserGameCommand command) throws ResponseException {
        try {
            wsSession.getBasicRemote().sendText(gson.toJson(command));
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }
    public void close() throws ResponseException {
        try {
            wsSession.close();
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }



    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
