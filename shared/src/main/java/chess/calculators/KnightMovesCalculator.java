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
        int[] shorts = {-1, 1};
        int[] longs = {-2, 2};
        int[] runs = {0, 1};
        for(int k : runs) {
            for (int i : shorts){
                for (int j : longs){
                    ChessPosition curPos = myPosition;
                        if (k == 0) {
                            curPos = new ChessPosition(curPos.getRow() + i, curPos.getColumn()+j);
                        } else {
                            curPos = new ChessPosition(curPos.getRow()+j, curPos.getColumn()+i);
                        }
                        if (curPos.getRow() < 1 || curPos.getColumn() < 1 || curPos.getRow() > 8 || curPos.getColumn() > 8) {
                            continue;
                        }
                        if (board.getPiece(curPos) == null) {
                            legalMoves.add(new ChessMove(myPosition, curPos, null));
                        } else {
                            if (board.getPiece(curPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                                legalMoves.add(new ChessMove(myPosition, curPos, null));
                            }
                        }
                }
            }
        }
        return legalMoves;
    }
}
