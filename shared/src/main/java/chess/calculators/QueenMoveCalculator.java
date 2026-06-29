package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMoveCalculator implements PieceMoveCalculator{

    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {  {0,1}, {1,0},
                                {-1,0},{0,-1},
                                {1,1}, {1,-1},
                                {-1,1},{-1,-1}};
        return slidingPieces(directions, board, myPosition);
    }
}
