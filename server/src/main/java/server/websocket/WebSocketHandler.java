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
        UserGameCommand base = gson.fromJson(ctx.message(), UserGameCommand.class);
        AuthData auth = dataAccess.getAuth(base.getAuthToken());
        if (auth == null){
            ctx.send((gson.toJson(new RuntimeException("Error: Invalid Auth"))));
            return;
        }
        String username = auth.username();

        switch (base.getCommandType()){
            case CONNECT -> {connect(ctx, base, username);}
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);
                makeMove(ctx, makeMoveCommand, username);
            }

        };


    }

    private void onClose(WsCloseContext ctx) {

    }

    private void onError(WsErrorContext ctx) {

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
            ctx.send(gson.toJson(new ErrorMessage("Error: Oberservers cannot make moves.")));
            return;
        }
        if (game.getTeamTurn() != role) {
            ctx.send(gson.toJson(new ErrorMessage("Error: Cannot move out of turn.")));
            return;
        }
            try {
                game.makeMove(cmd.getMove());
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e.getMessage());
            }
            dataAccess.updateGame(gameData);
            connections.broadcast(gameData.gameID(), null,
                    gson.toJson(new LoadGameMessage(game)));
            String piece = String.valueOf(game.getBoard().getPiece(cmd.getMove().getStartPosition()));
            String move = chessMoveToString(cmd.getMove());
            connections.broadcast(gameData.gameID(), cmd.getAuthToken(),
                    gson.toJson(new NotificationMessage(username + " moved " + piece + " from " + move)));

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
        String text = username + " joined as" + role;
        connections.broadcast(gameData.gameID(), cmd.getAuthToken(), gson.toJson(new NotificationMessage(text)));


    }
}
