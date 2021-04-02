package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;
import chess.util.Util;

public class ChessMatch {

    private int round;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkmate;

    private final ChessPiece[] piecesOnTheBoard = new ChessPiece[32];
    private final ChessPiece[] capturedPieces = new ChessPiece[32];


    public ChessMatch() {
        board = new Board(8, 8);
        round = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }


    public int getRound() { return round; }
    public Color getCurrentPlayer() { return currentPlayer; }
    public boolean isCheck() { return check; }
    public boolean isCheckmate() { return checkmate; }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                matrix[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return matrix;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePos) {
        Position position = sourcePos.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePos, ChessPosition targetPos) {
        Position source = sourcePos.toPosition();
        Position target = targetPos.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Illegal move: One does not simply self-check.");
        }

        check = testCheck(enemy(currentPlayer));

        if (testCheckmate(enemy(currentPlayer))) {
            checkmate = true;
        }
        else {
            nextRound();
        }
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);

        Util.arrRemove((ChessPiece) capturedPiece, piecesOnTheBoard);
        Util.arrAdd(capturedPiece, capturedPieces);

        board.placePiece(p, target);
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            Util.arrRemove((ChessPiece) capturedPiece, capturedPieces);
            Util.arrAdd(capturedPiece, piecesOnTheBoard);
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Source position is empty.");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException(String.format("Current turn is %ss'",
                    currentPlayer.toString()));
        }
        if (!board.piece(position).canMove()) {
            throw new ChessException("This piece is stuck.");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).isMovePossible(target)) {
            throw new ChessException("Invalid movement for chosen piece.");
        }
    }

    private void nextRound() {
        round++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color enemy(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        for (ChessPiece c : piecesOnTheBoard) {
            if (c != null && c.getColor() == color && c instanceof King) {
                return c;
            }
        }
        throw new IllegalStateException(String.format(
                "%s king not found on board.", color));
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();

        ChessPiece[] enemies = new ChessPiece[16];
        for (ChessPiece c : piecesOnTheBoard) {
            if (c != null) {
                if (c.getColor() == enemy(color)) {
                    Util.arrAdd(c, enemies);
                }
            }
        }

        for (ChessPiece c : enemies) {
            if (c != null) {
                boolean[][] matrix = c.possibleMoves();
                if (matrix[kingPosition.getRow()][kingPosition.getColumn()]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean testCheckmate(Color color) {
        if (!testCheck(color)) {
            return false;
        }

        ChessPiece[] allies = new ChessPiece[15];
        for (ChessPiece c : piecesOnTheBoard) {
            if (c != null) {
                if (c.getColor() == color) {
                    Util.arrAdd(c, allies);
                }
            }
        }

        for (ChessPiece c : allies) {
            if (c != null) {
                boolean[][] matrix = c.possibleMoves();
                for (int i = 0; i < board.getRows(); i++) {
                    for (int j = 0; j < board.getColumns(); j++) {
                        if (matrix[i][j]) {
                            Position source = c.getChessPosition().toPosition();
                            Position target = new Position(i, j);

                            ChessPiece capturedPiece = (ChessPiece) makeMove(source, target);

                            if (!testCheck(color)) {
                                undoMove(source, target, capturedPiece);
                                return false;
                            }
                            undoMove(source, target, capturedPiece);
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        Util.arrAdd(piece, piecesOnTheBoard);
    }

    private void initialSetup() {
        placeNewPiece('A', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('B', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('C', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('D', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('E', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('F', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('G', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('H', 2, new Pawn(board, Color.BLACK));
        placeNewPiece('A', 1, new Rook(board, Color.BLACK));
        placeNewPiece('H', 1, new Rook(board, Color.BLACK));
        placeNewPiece('B', 1, new Knight(board, Color.BLACK));
        placeNewPiece('G', 1, new Knight(board, Color.BLACK));
        placeNewPiece('C', 1, new Bishop(board, Color.BLACK));
        placeNewPiece('F', 1, new Bishop(board, Color.BLACK));
        placeNewPiece('E', 1, new King(board, Color.BLACK));

        placeNewPiece('A', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('B', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('C', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('D', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('E', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('F', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('G', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('H', 7, new Pawn(board, Color.WHITE));
        placeNewPiece('A', 8, new Rook(board, Color.WHITE));
        placeNewPiece('H', 8, new Rook(board, Color.WHITE));
        placeNewPiece('B', 8, new Knight(board, Color.WHITE));
        placeNewPiece('G', 8, new Knight(board, Color.WHITE));
        placeNewPiece('C', 8, new Bishop(board, Color.WHITE));
        placeNewPiece('F', 8, new Bishop(board, Color.WHITE));
        placeNewPiece('E', 8, new King(board, Color.WHITE));
    }
}
