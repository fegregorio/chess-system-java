package application;

import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        }
        catch (RuntimeException e){
            throw new InputMismatchException("Invalid combination." +
                    "Valid expressions range from A1 to H8.");
        }
    }

    public static void printBoard(ChessPiece[][] pieces){
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j]);
            }
            System.out.println();
        }
    }

    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print("-");
        }
        else {
            switch (piece.getColor()) {
                case WHITE:
                    System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
                    break;

                case BLACK:
                    System.out.print(ANSI_PURPLE + piece + ANSI_RESET);
                    break;
            }
        }
        System.out.print(" ");
    }
}
