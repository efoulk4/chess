package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> CalculateLegalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> legalMoves = new ArrayList<>();
        int[] directions = {-1, 1};
        for (int i : directions) {
            for (int j : directions) {
                ChessPosition curPos = myPosition;
                while (true) {
                    curPos = new ChessPosition(curPos.getRow() + i, curPos.getColumn() + j);
                    if (curPos.getRow() < 1 || curPos.getColumn() < 1 || curPos.getRow() > 8 || curPos.getColumn() > 8) {
                        break;
                    }
                    if (board.getPiece(curPos) == null) {
                        legalMoves.add(new ChessMove(myPosition, curPos, null));
                    } else {
                        if (board.getPiece(curPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            legalMoves.add(new ChessMove(myPosition, curPos, null));
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return legalMoves;
    }
}
