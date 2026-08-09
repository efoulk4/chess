package server.Websocket;

import io.javalin.websocket.WsContext;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> outer = new ConcurrentHashMap<>();
    public void add(Integer gameID, String authToken, String username, WsContext session){
    ConcurrentHashMap<String, Connection> inner;
    inner = outer.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>());
    inner.put(authToken, new Connection(username,session));
    }
    public void remove(Integer gameID, String authToken){
        ConcurrentHashMap<String, Connection> inner = outer.get(gameID);
        if (inner == null){
            throw new RuntimeException("Error: Tried to remove from a non-existent game");
        }
        inner.remove(authToken);
    }
    public void broadcast(Integer gameID, String excludedAuthToken, String message){
        ConcurrentHashMap<String, Connection> inner = outer.get(gameID);
        if (inner == null){
            throw new RuntimeException("Error: Tried to broadcast from a non-existent game");
        }
        ArrayList<String> deadTokens = new ArrayList<>();
        for (String token : inner.keySet()){
            if (Objects.equals(token, excludedAuthToken)){
                continue;
            }
            Connection conn = inner.get(token);
            try {
                conn.session().send(message);
            }
            catch (Exception e){
                deadTokens.add(token);
            }
        }
        for (String token : deadTokens){
            remove(gameID, token);
        }
    }
}
