package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;
import chess.util.Util;

import java.security.InvalidParameterException;

public class ChessMatch {

    private int round;
    private Color currentPlayer;
    private final Board board;
    private boolean check;
    private boolean checkmate;
    private ChessPiece vulnerablePawn;
    private ChessPiece promotedPawn;

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
    public ChessPiece getVulnerablePawn() { return vulnerablePawn; }
    public ChessPiece getPromotedPawn() { return promotedPawn; }

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

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // promotion
        promotedPawn = null;
        if (movedPiece instanceof Pawn && (target.getRow() == 0 || target.getRow() == 7)) {
            promotedPawn = (ChessPiece) board.piece(target);
            promotedPawn = replacePromotedPawn("Q");
        }

        check = testCheck(enemy(currentPlayer));

        if (testCheckmate(enemy(currentPlayer))) {
            checkmate = true;
        }
        else {
            nextRound();
        }

        // test for en passant
        if (movedPiece instanceof Pawn && (
                (target.getRow() == source.getRow() - 2) ||
                (target.getRow() == source.getRow() + 2))) {
            vulnerablePawn = movedPiece;
        }
        else {
            vulnerablePawn = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPawn(String piece) {
        if (promotedPawn == null) {
            throw new IllegalStateException("There is no piece to be promoted.");
        }
        if (!piece.equals("Q") && !piece.equals("R") && !piece.equals("H") && !piece.equals("B")) {
            throw new InvalidParameterException("Invalid piece. Valid options are Q, R, H and B");
        }

        Position pos = promotedPawn.getChessPosition().toPosition();
        ChessPiece p = (ChessPiece) board.removePiece(pos);
        Util.arrRemove(p, piecesOnTheBoard);

        ChessPiece newPiece = newPiece(piece, promotedPawn.getColor());
        board.placePiece(newPiece, pos);
        Util.arrAdd(newPiece, piecesOnTheBoard);

        return newPiece;
    }

    private ChessPiece newPiece(String piece, Color color) {
        switch (piece) {
            case "B":
                return new Bishop(board, color);

            case "H":
                return new Knight(board, color);

            case "R":
                return new Rook(board, color);

            default:
                return new Queen(board, color);
        }
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);

        Util.arrRemove((ChessPiece) capturedPiece, piecesOnTheBoard);
        Util.arrAdd(capturedPiece, capturedPieces);
        board.placePiece(p, target);

        // castling short
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() + 3);
            Position rookTarget = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rook, rookTarget);
            rook.increaseMoveCount();
        }

        // castling long
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() - 4);
            Position rookTarget = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rook, rookTarget);
            rook.increaseMoveCount();
        }

        // en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position capturedPawnPos;
                switch (p.getColor()) {
                    case WHITE:
                        capturedPawnPos = new Position(target.getRow() + 1, target.getColumn());
                        break;
                    case BLACK:
                        capturedPawnPos = new Position(target.getRow() - 1, target.getColumn());
                        break;
                    default:
                        throw new ChessException("No idea what happened.");
                }
                capturedPiece = board.removePiece(capturedPawnPos);
                Util.arrRemove(vulnerablePawn, piecesOnTheBoard);
                Util.arrAdd(vulnerablePawn, capturedPieces);
            }
        }

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

        // castling short
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() + 3);
            Position rookTarget = new Position(source.getRow(), source.getColumn() + 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rook, rookSource);
            rook.decreaseMoveCount();
        }

        // castling long
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position rookSource = new Position(source.getRow(), source.getColumn() - 4);
            Position rookTarget = new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rook, rookSource);
            rook.decreaseMoveCount();
        }

        // en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position capturedPawnPos;
                switch (p.getColor()) {
                    case WHITE:
                        capturedPawnPos = new Position(3, target.getColumn());
                        break;
                    case BLACK:
                        capturedPawnPos = new Position(4, target.getColumn());
                        break;
                    default:
                        throw new ChessException("No idea what happened.");
                }
                board.placePiece(pawn, capturedPawnPos);
            }
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

    public boolean willCastleCheck(Color color, Position source, Position target) {
        ChessPiece king = (ChessPiece) makeMove(source, target);

        if (!testCheck(color)) {
            undoMove(source, target, king);
            return false;
        }
        undoMove(source, target, king);
        return true;
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

        ChessPiece[] allies = new ChessPiece[16];
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
        placeNewPiece('A', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('B', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('C', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('D', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('E', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('F', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('G', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('H', 2, new Pawn(board, Color.BLACK, this));
        placeNewPiece('A', 1, new Rook(board, Color.BLACK));
        placeNewPiece('H', 1, new Rook(board, Color.BLACK));
        placeNewPiece('B', 1, new Knight(board, Color.BLACK));
        placeNewPiece('G', 1, new Knight(board, Color.BLACK));
        placeNewPiece('C', 1, new Bishop(board, Color.BLACK));
        placeNewPiece('F', 1, new Bishop(board, Color.BLACK));
        placeNewPiece('D', 1, new Queen(board, Color.BLACK));
        placeNewPiece('E', 1, new King(board, Color.BLACK, this));

        placeNewPiece('A', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('B', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('C', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('D', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('E', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('F', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('G', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('H', 7, new Pawn(board, Color.WHITE, this));
        placeNewPiece('A', 8, new Rook(board, Color.WHITE));
        placeNewPiece('H', 8, new Rook(board, Color.WHITE));
        placeNewPiece('B', 8, new Knight(board, Color.WHITE));
        placeNewPiece('G', 8, new Knight(board, Color.WHITE));
        placeNewPiece('C', 8, new Bishop(board, Color.WHITE));
        placeNewPiece('F', 8, new Bishop(board, Color.WHITE));
        placeNewPiece('D', 8, new Queen(board, Color.WHITE));
        placeNewPiece('E', 8, new King(board, Color.WHITE, this));
    }
}
