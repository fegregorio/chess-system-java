package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    private final ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }


    public boolean canMoveTo(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // up
        p.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // down
        p.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // left
        p.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // right
        p.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // upleft
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // upright
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downleft
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // downright
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMoveTo(p)) {
            matrix[p.getRow()][p.getColumn()] = true;
        }

        // castling short
        if (getMoveCount() == 0 && !chessMatch.isCheck()) {
            p.setValues(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(p)) {
                p.setValues(position.getRow(), position.getColumn() + 1);
                if (!getBoard().thereIsAPiece(p) && !chessMatch.willCastleCheck(getColor(), position, p)) {
                    p.setValues(position.getRow(), position.getColumn() + 2);
                    if (!getBoard().thereIsAPiece(p) && !chessMatch.willCastleCheck(getColor(), position, p)) {
                        matrix[p.getRow()][p.getColumn()] = true;
                    }
                }
            }
        }

        // castling long
        if (getMoveCount() == 0 && !chessMatch.isCheck()) {
            p.setValues(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(p)) {
                p.setValues(position.getRow(), position.getColumn() - 1);
                if (!getBoard().thereIsAPiece(p) && !chessMatch.willCastleCheck(getColor(), position, p)) {
                    p.setValues(position.getRow(), position.getColumn() - 2);
                    if (!getBoard().thereIsAPiece(p) && !chessMatch.willCastleCheck(getColor(), position, p)) {
                        p.setValues(position.getRow(), position.getColumn() - 3);
                        if (!getBoard().thereIsAPiece(p)) {
                            matrix[p.getRow()][p.getColumn() + 1] = true;
                        }
                    }
                }
            }
        }

        return matrix;
    }
}
