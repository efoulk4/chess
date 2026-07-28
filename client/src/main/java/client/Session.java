package client;

import chess.ChessGame;

public class Session {
    private State state = State.PRELOGIN;
    private String authToken;
    private Integer gameID;
    private ChessGame.TeamColor color;

    public State getState() {return state;}
    public String authToken() { return authToken; }
    public Integer gameID() { return gameID; }
    public ChessGame.TeamColor color() { return color; }

    public void setState(State s){ this.state = s;}
    public void setAuthToken(String t) { this.authToken = t; }
    public void setGameID(Integer id) { this.gameID = id; }
    public void setColor(ChessGame.TeamColor c) { this.color = c; }
}