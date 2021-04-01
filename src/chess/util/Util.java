package chess.util;

import boardgame.Piece;
import chess.ChessPiece;

public class Util {

    public static int getLength(ChessPiece[] captured) {
        int count = 0;
        for (ChessPiece c : captured) {
            if (c != null) {
                count++;
            }
            else {
                break;
            }
        }
        return count;
    }

    public static void arrRemove(ChessPiece captured, ChessPiece[] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == captured) {
                pieces[i] = null;
                break;
            }
        }
    }

    public static void arrAdd(Piece piece, ChessPiece[] pieceArray) {
        if (piece != null) {
            pieceArray[getLength(pieceArray)] = (ChessPiece) piece;
        }
    }
}
