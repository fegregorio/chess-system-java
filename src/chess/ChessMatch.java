package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private Board board;


    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }


    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                matrix[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return matrix;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece('A', 1, new Rook(board, Color.BLACK));
        placeNewPiece('E', 1, new King(board, Color.BLACK));
        placeNewPiece('A', 8, new Rook(board, Color.WHITE));
        placeNewPiece('E', 8, new King(board, Color.WHITE));
    }
}
