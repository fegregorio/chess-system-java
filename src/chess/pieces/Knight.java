package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }


    public boolean canMoveTo(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public String toString() {
        return "H";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // upright
        p.setValues(position.getRow() - 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // upleft
        p.setValues(position.getRow() - 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // leftup
        p.setValues(position.getRow() - 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // leftdown
        p.setValues(position.getRow() + 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downleft
        p.setValues(position.getRow() + 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downright
        p.setValues(position.getRow() + 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // rightdown
        p.setValues(position.getRow() + 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // rightup
        p.setValues(position.getRow() - 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        return matrix;
    }
}
