package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }


    public boolean canMoveTo(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // upwards
        p.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downwards
        p.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // leftwards
        p.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // rightwards
        p.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // upleftwards
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // uprightwards
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downleftwards
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downrightwards
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        return matrix;
    }
}
