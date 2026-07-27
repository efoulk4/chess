package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;

import service.*;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        this.serverUrl = url;
    }

    public AuthData register (UserData user){
        var request = buildRequest("POST", "/user", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
        }
    public AuthData login (LoginRequest loginRequest){
        var request = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }
    public void logout (String authToken){
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }
    public void clear (){
        var request  = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }
    public Collection<GameData> getGames (String authToken){
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);
        ListGamesResult result = handleResponse(response, ListGamesResult.class);
        if (result == null){
            return new ArrayList<>();
        }
        return  result.games();
    }
    public CreateGameResult createGame (CreateGameRequest createGameRequest, String authToken){
        var request = buildRequest("POST", "/game", createGameRequest, authToken);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);
    }
    public void joinGame (JoinGameRequest joinGameRequest, String authToken){
        var request = buildRequest("PUT", "/game", joinGameRequest, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }



    private HttpRequest buildRequest (String method, String path, Object body, String authToken){
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null){
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null){
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }
    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest (HttpRequest request){
        try{
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception ex) {
            throw new RuntimeException("Error: Unexpected Client Side Error");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws RuntimeException {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body == null) {
                throw new RuntimeException("Error: Couldn't handle response -- empty body");
            }

            throw new RuntimeException("Error: Couldn't handle response -- other error" + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }
}
