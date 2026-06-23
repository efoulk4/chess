package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition);
    default boolean validateSlidingMove (ChessPosition myPosition ,ChessPosition curPos, ChessBoard board){
        if (curPos.getRow() < 1 || curPos.getColumn() < 1 || curPos.getRow() > 8 || curPos.getColumn() > 8) {
            return false;
        }
        if (board.getPiece(curPos) == null) {
            return true;
        } else {
            return board.getPiece(curPos).getTeamColor() != board.getPiece(myPosition).getTeamColor();
        }
    }
}
