package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] direction : directions) {
                ChessPosition curPos = myPosition;
                while (true) {
                    curPos = new ChessPosition(curPos.getRow() + direction[0], curPos.getColumn() + direction[1]);
                    if (validateMove(myPosition, curPos, board)){
                        legalMoves.add(new ChessMove(myPosition,curPos,null));
                        if (board.getPiece(curPos) != null){break;}
                    }
                    else {
                        break;
                    }
                }
        }
        return legalMoves;
    }
}
