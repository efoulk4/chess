package chess.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> calculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        boolean firstMove = false;
        boolean promotesOnNextMove = false;
        int dir = 1;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){dir *= -1;}
        if (myPosition.getRow() == 7){
            if (dir == 1){promotesOnNextMove = true;}
            else {firstMove = true;}
        }
        else if (myPosition.getRow() == 2) {
            if (dir == -1){promotesOnNextMove = true;}
            else {firstMove = true;}
        }
        Collection<ChessMove> legalMoves = new ArrayList<>();
        /*
        FORWARD MOVEMENT
         */
        ArrayList<Integer> spaces = new ArrayList<>();
        spaces.add(1);
        if (firstMove){spaces.add(2);}
        ChessPosition curPos = myPosition;
        for (int i : spaces){
            curPos = new ChessPosition(myPosition.getRow()+ i*dir, myPosition.getColumn());
            if (validateInBounds(curPos) && vacantSquare(board,curPos)){
                legalMoves = handlePromotion(legalMoves, myPosition, curPos, promotesOnNextMove);
            }
            else{
                break;
            }
            if (i ==2 ){
                handleEnpassant(board, curPos, dir);
            }
        }
        /*
        CAPTURING
         */
        int [] sides = {-1,1};
        for (int i : sides){
            curPos = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn() +i);
            if (validateInBounds(curPos) &&
                !vacantSquare(board,curPos) &&
                isValidCapture(board,myPosition, curPos)){

                        legalMoves = handlePromotion(legalMoves, myPosition, curPos, promotesOnNextMove);
                 }
        }

        return legalMoves;
    }
    public void handleEnpassant(ChessBoard board, ChessPosition resultantPosition, int dir){
        ChessPosition left = new ChessPosition(resultantPosition.getRow(), resultantPosition.getColumn()-1);
        ChessPosition right = new ChessPosition(resultantPosition.getRow(), resultantPosition.getColumn()+1);
        ChessPosition enpassantSquare =
                new ChessPosition(resultantPosition.getRow()-dir, resultantPosition.getColumn());
        for (ChessPosition pos : new ChessPosition[]{left, right}){
            ChessPiece piece = board.getPiece(pos);
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN){
                piece.setEnpassantAvailable(true);
                piece.setEnpassantPosition(enpassantSquare);
            }
        }
    }
}
