package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public TeamColor flipTeamColor(TeamColor team){
        return switch (team) {
            case WHITE -> TeamColor.BLACK;
            case BLACK -> TeamColor.WHITE;
        };
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null){
            return null;
        }
        else {
            ChessPiece piece = board.getPiece(startPosition);
            TeamColor team = piece.getTeamColor();
            Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
            Collection<ChessMove> validMoves = new ArrayList<>();
            ChessBoard realBoard = getBoard();
            for (ChessMove move :  pieceMoves){
                setBoard(new ChessBoard(realBoard));
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), piece);
                if (!isInCheck(team)){
                    validMoves.add(move);
                }
            }
            setBoard(realBoard);
            return validMoves;
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null||piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)){
            throw new InvalidMoveException();
        }

        ChessPiece.PieceType resultantPieceType = piece.getPieceType();
        if (move.getPromotionPiece() != null){
            resultantPieceType = move.getPromotionPiece();
        }
        board.removePiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),resultantPieceType));
        teamTurn = flipTeamColor(teamTurn);
    }

    Collection<ChessPosition> attackedSquares(ChessBoard board, TeamColor team){
        Collection<ChessPosition> attacked = new ArrayList<>();
        ChessPosition curPos;
        for (int row = 1; row < 9; row++){
            for (int column = 1; column < 9; column++){
                curPos = new ChessPosition(row, column);
                ChessPiece piece = board.getPiece(curPos);
                if (piece == null||piece.getTeamColor() != team){
                    continue;
                }
                Collection<ChessMove> pieceMoves = piece.pieceMoves(board, curPos);
                for (ChessMove move : pieceMoves){
                    attacked.add(move.getEndPosition());
                }
            }

        }
        return attacked;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition curPos = null;
        ChessPosition kingPos = null;
        for (int row = 1; row < 9; row++){
            for (int column = 1; column < 9; column++) {
                curPos = new ChessPosition(row, column);
                ChessPiece piece = board.getPiece(curPos);
                if (piece == null||piece.getTeamColor() != teamColor){
                    continue;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING){
                    kingPos = curPos;
                    break;
                }
            }
        }
        Collection<ChessPosition> attacked = attackedSquares(board, flipTeamColor(teamColor));
        return attacked.contains(kingPos);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !teamHasValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !teamHasValidMoves(teamColor);
    }


    public boolean teamHasValidMoves(TeamColor teamColor){
        ChessPosition curPos;
        for (int row = 1; row < 9; row++){
            for (int column = 1; column < 9; column++) {
                curPos = new ChessPosition(row, column);
                ChessPiece piece = board.getPiece(curPos);
                if (piece == null||piece.getTeamColor() != teamColor){
                    continue;
                }
                if (!validMoves(curPos).isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return teamTurn + "to play\n" +
                board;
    }
}
