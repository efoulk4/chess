package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0,-1}};
        for (int[] dir : directions){
                ChessPosition curPos = myPosition;
                while (true) {
                    curPos = new ChessPosition(curPos.getRow() + dir[0], curPos.getColumn() + dir[1]);
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
