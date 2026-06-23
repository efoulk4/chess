package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int[] directions = {-1, 0, 1};
        for (int i : directions) {
            for (int j : directions) {
                ChessPosition curPos = myPosition;
                    if (i == 0 && j ==0){continue;}
                    curPos = new ChessPosition(curPos.getRow() + i, curPos.getColumn() + j);
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
        return legalMoves;
    }
}
