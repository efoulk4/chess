package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
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
        try {
            UserGameCommand base = gson.fromJson(ctx.message(), UserGameCommand.class);
            AuthData auth = dataAccess.getAuth(base.getAuthToken());
            if (auth == null) {
                ctx.send(gson.toJson(new ErrorMessage("Error: Invalid Auth")));
                return;
            }
            String username = auth.username();

            switch (base.getCommandType()) {
                case CONNECT -> {
                    connect(ctx, base, username);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand makeMoveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);
                    makeMove(ctx, makeMoveCommand, username);
                }
                case LEAVE -> {leave(ctx, base, username);
                }
                case RESIGN -> resign(ctx, base, username);
            }
        }
        catch (Exception e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));
        }


    }

    private void onClose(WsCloseContext ctx) {
        connections.removeAll(ctx);
    }

    private void onError(WsErrorContext ctx) {

    }
    private void resign(WsMessageContext ctx, UserGameCommand cmd, String username){
        GameData gameData = dataAccess.getGame(cmd.getGameID());
        if (gameData == null){
            ctx.send(gson.toJson(new ErrorMessage("Error: No such game.")));
            return;
        }
        if (gameData.game().gameIsOver()){
            ctx.send(gson.toJson(new ErrorMessage("Error: Game Already Over.")));
            return;
        }
        ChessGame.TeamColor role = null;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            role = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            role = ChessGame.TeamColor.BLACK;
        }
        if (role == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: Observers shan't be resigning.")));
            return;
        }
        gameData.game().setResigned(role);
        dataAccess.updateGame(gameData);
        connections.broadcast(gameData.gameID(), null,
                gson.toJson(new NotificationMessage(username + " resigned.")));


    }
    private void leave(WsMessageContext ctx, UserGameCommand cmd, String username){
        GameData gameData = dataAccess.getGame(cmd.getGameID());
        if (gameData == null){
            ctx.send(gson.toJson(new ErrorMessage("Error: No such game.")));
            return;
        }
        GameData newGameData = null;
        if (Objects.equals(gameData.whiteUsername(), username)){
            newGameData =
            new GameData(gameData.gameID(),null, gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
        }
        if (Objects.equals(gameData.blackUsername(), username)){
            newGameData =
            new GameData(gameData.gameID(), gameData.whiteUsername(), null,
            gameData.gameName(), gameData.game());
        }
        if (newGameData!= null) {
            dataAccess.updateGame(newGameData);
        }
        connections.remove(gameData.gameID(), cmd.getAuthToken());
        connections.broadcast(gameData.gameID(), cmd.getAuthToken(),
                gson.toJson(new NotificationMessage(username + " left the game.")));
    }

    private void makeMove(WsMessageContext ctx, MakeMoveCommand cmd, String username) {
        GameData gameData = dataAccess.getGame(cmd.getGameID());
        if (gameData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: No such game.")));
            return;
        }
        ChessGame game = gameData.game();
        if (game.gameIsOver()) {
            ctx.send(gson.toJson(new ErrorMessage("Error: Game Already Over.")));
            return;
        }
        ChessGame.TeamColor role = null;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            role = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(gameData.blackUsername(), username)) {
            role = ChessGame.TeamColor.BLACK;
        }
        if (role == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: Observers cannot make moves.")));
            return;
        }
        if (game.getTeamTurn() != role) {
            ctx.send(gson.toJson(new ErrorMessage("Error: Cannot move out of turn.")));
            return;
        }
        String piece = String.valueOf(game.getBoard().getPiece(cmd.getMove().getStartPosition()));
            try {
                game.makeMove(cmd.getMove());
            } catch (InvalidMoveException e) {
                ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));
                return;
            }
            dataAccess.updateGame(gameData);
            connections.broadcast(gameData.gameID(), null,
                    gson.toJson(new LoadGameMessage(game)));
            String move = chessMoveToString(cmd.getMove());
            connections.broadcast(gameData.gameID(), cmd.getAuthToken(),
                    gson.toJson(new NotificationMessage(username + " moved " + piece + " from " + move)));
            ChessGame.TeamColor opponent = game.getTeamTurn();
            String status = null;
            String opponentName;
            if (role  == ChessGame.TeamColor.WHITE){
                opponentName = gameData.blackUsername();
            }
            else{
                opponentName = gameData.whiteUsername();
            }
            if (game.isInCheckmate(opponent)){status = opponentName  + " is in checkmate";}
            else if (game.isInStalemate(opponent)){status = "Stalemate";}
            else if (game.isInCheck(opponent)){status = opponentName + " is in check";}
            if (status != null){
                connections.broadcast(gameData.gameID(), null,
                        gson.toJson(new NotificationMessage(status)));
            }

    }
    String chessMoveToString(ChessMove move){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        String promo = "";
        if (move.getPromotionPiece() != null){
            promo = " " + move.getPromotionPiece();
        }
        return chessPositionToString(start) + " to " + chessPositionToString(end) + promo + ".";
    }
    String chessPositionToString(ChessPosition pos){
            return "" + (char)('a' + pos.getColumn() - 1) + pos.getRow();
    }


    private void connect(WsMessageContext ctx, UserGameCommand cmd, String username) {
        GameData gameData = dataAccess.getGame(cmd.getGameID());
        if (gameData == null){
            ctx.send(gson.toJson(new ErrorMessage("Error: No such game.")));
            return;
        }
        connections.add(cmd.getGameID(), cmd.getAuthToken(), username, ctx);
        ctx.send(gson.toJson(new LoadGameMessage(gameData.game())));

        String role = "an observer";
        if (Objects.equals(gameData.whiteUsername(), username)){role = "white";}
        if (Objects.equals(gameData.blackUsername(), username)){role = "black";}
        String text = username + " joined as " + role;
        connections.broadcast(gameData.gameID(), cmd.getAuthToken(), gson.toJson(new NotificationMessage(text)));


    }
}
