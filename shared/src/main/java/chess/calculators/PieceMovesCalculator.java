package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition);
    default boolean validateMove(ChessPosition myPosition , ChessPosition curPos, ChessBoard board){
        if (curPos.getRow() < 1 || curPos.getColumn() < 1 || curPos.getRow() > 8 || curPos.getColumn() > 8) {
            return false;
        }
        if (board.getPiece(curPos) == null) {
            return true;
        } else {
            return board.getPiece(curPos).getTeamColor() != board.getPiece(myPosition).getTeamColor();
        }
    }
    default Collection<ChessMove> handlePromotion(
            Collection<ChessMove> legalMoves,
            ChessPosition myPosition,
            ChessPosition curPos,
            boolean promotesOnNextMove)

    {
        ChessPiece.PieceType[] promotionOptions = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT
            };
        if (promotesOnNextMove){
            for (ChessPiece.PieceType type : promotionOptions){
                legalMoves.add(new ChessMove(myPosition, curPos, type));
            }
        }
        else {
            legalMoves.add(new ChessMove(myPosition, curPos, null));
        }
        return legalMoves;
    }
}
