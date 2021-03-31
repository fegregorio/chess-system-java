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
        }
        return count;
    }

    public static void arrRemove(Piece captured, ChessPiece[] pieces) {
        if (captured != null) {
            for (int i = 0; i < Util.getLength(pieces); i++) {
                if (pieces[i] == captured) {
                    pieces[i] = null;
                }
            }
        }
    }

    public static void arrAdd(Piece piece, ChessPiece[] pieceArray) {
        if (piece != null) {
            pieceArray[getLength(pieceArray)] = (ChessPiece) piece;
        }
    }
}
