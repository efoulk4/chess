package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new ArrayList<>();

        PieceMovesCalculator rookCalc = new RookMovesCalculator();
        queenMoves.addAll(rookCalc.calculateLegalMoves(board,myPosition));

        PieceMovesCalculator bishopCalc = new BishopMovesCalculator();
        queenMoves.addAll(bishopCalc.calculateLegalMoves(board,myPosition));

        return queenMoves;
    }
}
