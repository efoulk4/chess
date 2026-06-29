package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMoveCalculator {
    Collection<ChessMove> calculateLegalMoves (ChessBoard board, ChessPosition myPosition);

    default boolean validateMove(ChessBoard board, ChessPosition myPosition, ChessPosition curPos){
        if (!validateInBounds(curPos)){
            return false;
        }
        if (vacantSquare(board, curPos)){
            return true;
        }
        return isValidCapture(board,myPosition,curPos);
    }
    default boolean validateInBounds (ChessPosition curPos){
        return curPos.getRow() > 0 &&
                curPos.getColumn() > 0 &&
                curPos.getRow() < 9 &&
                curPos.getColumn() < 9;
    }
    default boolean vacantSquare (ChessBoard board, ChessPosition curPos){
        return board.getPiece(curPos) == null;
    }
    default boolean isValidCapture (ChessBoard board, ChessPosition myPosition, ChessPosition curPos){
        return board.getPiece(myPosition).getTeamColor() != board.getPiece(curPos).getTeamColor();
    }

    default Collection<ChessMove> slidingPieces (int[][] directions,
                                                 ChessBoard board,
                                                 ChessPosition myPosition){
        Collection<ChessMove> legalMoves = new ArrayList<>();
        for (int[] direction : directions){
            ChessPosition curPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
            while (true){
                curPos = new ChessPosition(curPos.getRow()+direction[0], curPos.getColumn()+direction[1]);
                if (validateMove(board, myPosition, curPos)){
                    legalMoves.add(new ChessMove(myPosition, curPos, null));
                    if (board.getPiece(curPos) != null){break;}
                }
                else {
                    break;
                }
            }
        }
        return legalMoves;
    }

    default Collection<ChessMove> nonSlidingPieces (int[][] directions,
                                                 ChessBoard board,
                                                 ChessPosition myPosition){
        Collection<ChessMove> legalMoves = new ArrayList<>();
        for (int[] direction : directions){
            ChessPosition curPos = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
                curPos = new ChessPosition(curPos.getRow()+direction[0], curPos.getColumn()+direction[1]);
                if (validateMove(board, myPosition, curPos)){
                    legalMoves.add(new ChessMove(myPosition, curPos, null));
                }
        }
        return legalMoves;
    }
    default Collection<ChessMove> handlePromotion (Collection<ChessMove> legalMoves,
                                                    ChessPosition myPosition,
                                                   ChessPosition curPos,
                                                   boolean promotesOnNextMove){
        if (promotesOnNextMove){
            ChessPiece.PieceType[] types = new ChessPiece.PieceType[]{
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.KNIGHT,
                    ChessPiece.PieceType.BISHOP
            };
            for (ChessPiece.PieceType type : types){
                legalMoves.add(new ChessMove(myPosition, curPos, type));
            }
        }
        else{
            legalMoves.add(new ChessMove(myPosition, curPos, null));
        }
        return legalMoves;
    }
}


