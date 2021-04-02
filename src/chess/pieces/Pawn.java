package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    public Pawn(Board board, Color color) { super(board, color); }


    @Override
    public String toString() { return "p"; }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        switch (getColor()) {
            case WHITE:
                // movement
                p.setValues(position.getRow() - 1, position.getColumn());
                if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                    matrix[p.getRow()][p.getColumn()] = true;
                }
                if (getMoveCount() == 0) {
                    p.setValues(position.getRow() - 2, position.getColumn());
                    if (matrix[p.getRow() + 1][p.getColumn()] && !getBoard().thereIsAPiece(p)) {
                        matrix[p.getRow()][p.getColumn()] = true;
                    }
                }

                // capture
                p.setValues(position.getRow() - 1, position.getColumn() - 1);
                if (getBoard().positionExists(p) && isThereAnEnemy(p)) {
                    matrix[p.getRow()][p.getColumn()] = true;
                }
                p.setValues(position.getRow() - 1, position.getColumn() + 1);
                if (getBoard().positionExists(p) && isThereAnEnemy(p)) {
                    matrix[p.getRow()][p.getColumn()] = true;
                }
                break;

            case BLACK:
                // movement
                p.setValues(position.getRow() + 1, position.getColumn());
                if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                    matrix[p.getRow()][p.getColumn()] = true;
                }
                if (getMoveCount() == 0) {
                    p.setValues(position.getRow() + 2, position.getColumn());
                    if (matrix[p.getRow() - 1][p.getColumn()] && !getBoard().thereIsAPiece(p)) {
                        matrix[p.getRow()][p.getColumn()] = true;
                    }
                }

                // capture
                p.setValues(position.getRow() + 1, position.getColumn() - 1);
                if (getBoard().positionExists(p) && isThereAnEnemy(p)) {
                    matrix[p.getRow()][p.getColumn()] = true;
                }
                p.setValues(position.getRow() + 1, position.getColumn() + 1);
                if (getBoard().positionExists(p) && isThereAnEnemy(p)) {
                    matrix[p.getRow()][p.getColumn()] = true;
                }
                break;
        }
        return matrix;
    }
}
