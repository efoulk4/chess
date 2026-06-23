package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int[] directions = {-1, 1};
        for (int i : directions) {
            for (int j : directions) {
                ChessPosition curPos = myPosition;
                while (true) {
                    curPos = new ChessPosition(curPos.getRow() + i, curPos.getColumn() + j);
                    if (validateSlidingMove(myPosition, curPos, board)){
                        legalMoves.add(new ChessMove(myPosition,curPos,null));
                        if (board.getPiece(curPos) != null){break;}
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }
}
