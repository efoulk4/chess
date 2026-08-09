package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardRenderer {

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective){

        boolean white = (perspective != ChessGame.TeamColor.BLACK);
        int rowStart = white ? 8 : 1;
        int rowEnd   = white ? 1 : 8;
        int rowStep  = white ? -1 : 1;

        int colStart = white ? 1 : 8;
        int colEnd = white ? 8 : 1;
        int colStep = white ? 1 : -1;
        StringBuilder sb = new StringBuilder();

        sb.append(columnLabels(colStart, colEnd, colStep));

        for (int row = rowStart; row != rowEnd + rowStep; row += rowStep) {
            sb.append(rankLabel(row));
            for (int col = colStart; col != colEnd + colStep; col += colStep) {
                boolean lightSquare = (row + col) % 2 != 0;
                String bg = lightSquare ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY;
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String pieceImage = getPieceImage(piece);
                String textColor = (piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE) ?
                        SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
                sb.append(bg).append(textColor).append(pieceImage).append(RESET);
            }
            sb.append(rankLabel(row));
            sb.append("\n");
        }

        sb.append(columnLabels(colStart, colEnd, colStep));
        return sb.toString();
    }

    private static String columnLabels(int colStart, int colEnd, int colStep) {
        StringBuilder sb = new StringBuilder();
        sb.append(SET_BG_COLOR_DARK_GREY).append(SET_TEXT_COLOR_LIGHT_GREY);
        sb.append(EMPTY);
        for (int col = colStart; col != colEnd + colStep; col += colStep) {
            char letter = (char) ('a' + (col - 1));
            sb.append(" ").append(letter).append(" ");
        }
        sb.append(EMPTY);
        sb.append(RESET).append("\n");
        return sb.toString();
    }

    private static String rankLabel(int row) {
        return SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY + " " + row + " " + RESET;
    }

    public static String getPieceImage(ChessPiece piece){
        if (piece == null){
            return EMPTY;
        }
        boolean white = piece.getTeamColor()== ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()){
            case KING -> white ? WHITE_KING : BLACK_KING;
            case QUEEN -> white ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> white ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> white ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> white ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> white ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}
