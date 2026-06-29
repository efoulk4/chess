package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMoveCalculator implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1,1}, {1,-1}, {-1,1},{-1,-1}};
        return slidingPieces(directions, board, myPosition);
    }
}
