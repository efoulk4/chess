package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int [][] potentials = {{1,2}, {1,-2},
                               {-1,2}, {-1,-2},
                               {2,1}, {2,-1},
                               {-2,1}, {-2,-1}};
            for (int[] move : potentials){
                ChessPosition curPos = myPosition;
                    curPos = new ChessPosition(myPosition.getRow()+ move[0],
                                                myPosition.getColumn()+ move[1]);
                if (validateMove(myPosition, curPos, board)){
                    legalMoves.add(new ChessMove(myPosition,curPos,null));
                }

        }
        return legalMoves;
    }
}
