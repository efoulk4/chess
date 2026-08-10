package client;

import com.google.gson.Gson;
import exceptions.ResponseException;
import jakarta.websocket.*;
import jakarta.websocket.Session;
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

            //set message handler
            this.wsSession.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    var type = serverMessage.getServerMessageType();
                    switch (type){
                        case NOTIFICATION -> {}
                        case LOAD_GAME -> {}
                        case ERROR -> {}
                        case null, default -> {};
                    }
                    serverMessageHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }



    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
