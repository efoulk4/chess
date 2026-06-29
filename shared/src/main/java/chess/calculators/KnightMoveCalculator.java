package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class KnightMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1,2}, {1,-2},
                              {-1,2},{-1,-2},
                              {2,1}, {2,-1},
                              {-2,1},{-2,-1}};
        return nonSlidingPieces(directions, board, myPosition);
    }
}
