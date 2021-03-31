package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;
import chess.util.Util;

public class ChessMatch {

    private int round;
    private Color currentPlayer;
    private Board board;

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
        nextRound();
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);

        Util.arrRemove(capturedPiece, piecesOnTheBoard);
        Util.arrAdd(capturedPiece, capturedPieces);

        board.placePiece(p, target);
        return capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Source position is empty.");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException(String.format("Current turn is %s's",
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

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard[Util.getLength(piecesOnTheBoard)] = piece;
    }

    private void initialSetup() {
        placeNewPiece('A', 1, new Rook(board, Color.BLACK));
        placeNewPiece('E', 1, new King(board, Color.BLACK));
        placeNewPiece('A', 8, new Rook(board, Color.WHITE));
        placeNewPiece('E', 8, new King(board, Color.WHITE));
    }
}
