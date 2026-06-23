package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> CalculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> QueenMoves = new ArrayList<>();

        PieceMovesCalculator rookCalc = new RookMovesCalculator();
        QueenMoves.addAll(rookCalc.CalculateLegalMoves(board,myPosition));

        PieceMovesCalculator bishopCalc = new BishopMovesCalculator();
        QueenMoves.addAll(bishopCalc.CalculateLegalMoves(board,myPosition));

        return QueenMoves;
    }
}
