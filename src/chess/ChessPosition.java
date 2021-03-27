package chess;

import boardgame.Position;

public class ChessPosition {

    private char column;
    private int row;


    public ChessPosition(char column, int row) {
        if (column < 'A' || column > 'H' || row < 1 || row > 8) {
            throw new ChessException("Invalid combination. " +
                    "Valid expressions range from A1 to H8.");
        }
        this.column = column;
        this.row = row;
    }


    public char getColumn() { return column; }
    public int getRow() { return row; }

    protected Position toPosition() {
        return new Position(row - 1, column - 'A');
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char)('A' - position.getColumn()), position.getRow() + 1);
    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}
