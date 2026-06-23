package chess.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int dir = 1;
        boolean firstMove = false;
        boolean promotesOnNextMove = false;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){dir *= -1;}
        if (myPosition.getRow() == 7) {
            if (dir == -1){firstMove = true;}
            else{promotesOnNextMove = true;}
        }
        else if (myPosition.getRow() == 2) {
            if (dir == 1){firstMove = true;}
            else{promotesOnNextMove = true;}
        }
        /*
        CAPTURING
         */
        int[] sides = {-1, 1};
        for (int i : sides) {
            ChessPosition curPos = myPosition;
            curPos = new ChessPosition(curPos.getRow() + dir, curPos.getColumn() + i);
            if (curPos.getRow() < 1 || curPos.getColumn() < 1 || curPos.getRow() > 8 || curPos.getColumn() > 8) {
                continue;
            }
            if (board.getPiece(curPos) == null) {
                continue;
            }
            if (board.getPiece(curPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                legalMoves = handlePromotion(legalMoves, myPosition, curPos, promotesOnNextMove);
            }
        }
        /*
        FORWARD MOVEMENT
         */
        ChessPosition curPos = myPosition;
        ArrayList<Integer> steps = new ArrayList<>();
        steps.add(1);
        if (firstMove){steps.add(2);}
        for (int i : steps) {
            curPos = new ChessPosition(myPosition.getRow() + i*dir, myPosition.getColumn());
            if (curPos.getRow() < 1 || curPos.getColumn() < 1 || curPos.getRow() > 8 || curPos.getColumn() > 8) {
                continue;
            }
            if (board.getPiece(curPos) == null) {
                legalMoves = handlePromotion(legalMoves, myPosition, curPos, promotesOnNextMove);
            } else {
                break;
            }
            }
        return legalMoves;
    }
}
