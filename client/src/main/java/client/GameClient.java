package client;

import chess.*;
import exceptions.ResponseException;
import model.CreateGameRequest;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.*;

import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class GameClient {
    private final Session session;
    private final Scanner scanner;
    public GameClient(Session session, Scanner scanner) {
        this.session = session;
        this.scanner = scanner;
    }

    public String eval(String input) throws ResponseException {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> BoardRenderer.draw(session.getGame().getBoard(), session.color(), null, null);
            case "resign" -> resign();
            case "move" -> move(params);
            case "highlight" -> highlight(params);
            case "leave" -> leave();
            case "quit" -> "quit";
            default -> help();
        };
    }
    private String highlight(String ...params) throws ResponseException{
        if (params.length == 1){
            String coord = params[0].toLowerCase();
            ChessPosition pos = stringToPosition(coord);
            if (session.getGame().getBoard().getPiece(pos) == null){
                return "No piece there!";
            }
            Collection<ChessMove> legalMoves = session.getGame().validMoves(pos);
            Collection<ChessPosition> endSquares = new ArrayList<>();
            for (ChessMove move : legalMoves) {
                endSquares.add(move.getEndPosition());
            }
            return BoardRenderer.draw(session.getGame().getBoard(), session.color(), endSquares, pos);
        }
        else{
            throw new RuntimeException("Expected: highlight <CHESS COORDINATE>");
        }
    }

    private String move(String ...params) throws ResponseException{
        if (params.length == 2 || params.length == 3) {
            String coord1 = params[0];
            String coord2 = params[1];
            ChessPiece.PieceType promotion;
            promotion = params.length == 3 ? stringToPiece(params[2]) : null;
            ChessPosition cp1 = stringToPosition(coord1);
            ChessPosition cp2 = stringToPosition(coord2);
            ChessMove chessMove = new ChessMove(cp1, cp2, promotion);
            session.getFacade().send(
                new MakeMoveCommand(session.authToken(), session.gameID(), chessMove));
            return "";
        } else {
            throw new RuntimeException("Expected: move <CHESS COORDINATE> <CHESS COORDINATE>");
        }
    }
    private ChessPiece.PieceType stringToPiece(String piece){
        try {
            ChessPiece.PieceType pieceType = ChessPiece.PieceType.valueOf(piece.toUpperCase());
            if (pieceType == ChessPiece.PieceType.KING || pieceType == ChessPiece.PieceType.PAWN){
                throw new RuntimeException("Promotion must be QUEEN, ROOK, BISHOP, or KNIGHT");
            }
            else return pieceType;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Promotion must be QUEEN, ROOK, BISHOP, or KNIGHT");
        }
    }
    private ChessPosition stringToPosition(String square){
        String lowerCase = square.toLowerCase();
        if (lowerCase.length() != 2){
            throw new RuntimeException("Input only valid positions on the chess board");
        }
        int col = lowerCase.charAt(0) - 'a' + 1;
        int row = lowerCase.charAt(1) - '0';
        if (col < 1 || col > 8 || row < 1 || row > 8){
            throw new RuntimeException("Input only valid positions on the chess board");
        }
        return new ChessPosition(row, col);
    }
    private String resign() throws ResponseException {

            String result = "";
            System.out.print("Are you sure you want to resign? <y/n> \n");
            result = scanner.nextLine();
            result = result.toLowerCase();
            if (Objects.equals(result, "y")||Objects.equals(result, "yes")){
                session.getFacade().send(
                new UserGameCommand(UserGameCommand.CommandType.RESIGN, session.authToken(), session.gameID()));
                return "";
            }
            else {
                return "Resignation cancelled";
            }

    }
    private String leave() throws ResponseException {
        session.getFacade().send(new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                                                    session.authToken(),
                                                    session.gameID()));
        session.getFacade().close();
        session.setState(State.POSTLOGIN);
        session.setGameID(null);
        session.setColor(null);
        return "Successfully left the game";
    }

    public String help() {
        return """
                  - move <CHESS COORDINATE> <CHESS COORDINATE> - a piece
                  - resign - the game
                  - highlight <CHESS COORDINATE> - the legal moves for that piece
                  - redraw - the board
                  - leave - the game
                  - help - with possible commands
                  """;
    }
}
