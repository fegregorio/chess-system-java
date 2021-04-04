package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;
import chess.util.Util;
import org.w3c.dom.ls.LSOutput;

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

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();

            if (s.equals("exit")) {
                System.exit(0);
            }

            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        }
        catch (RuntimeException e){
            throw new InputMismatchException("Invalid combination. " +
                    "Valid expressions range from A1 to H8.");
        }
    }

    public static void printMatch(ChessMatch chessMatch, ChessPiece[] captured) {
        printBoard(chessMatch.getPieces());
        printCapturedPieces(captured);
        System.out.println("Round: " + chessMatch.getRound());

        if (!chessMatch.isCheckmate()) {
            System.out.printf("%ss' turn. ", chessMatch.getCurrentPlayer().toString());
            if (chessMatch.isCheck()) {
                System.out.print("Check.");
            }
        }
        else {
            System.out.println("Checkmate.");
            System.out.printf("Winner: %s", chessMatch.getCurrentPlayer());
        }
        System.out.println();
    }

    public static void printBoard(ChessPiece[][] pieces){
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printPiece(ChessPiece piece, boolean backgroundColor) {
        if (backgroundColor) {
            System.out.print(ANSI_GREEN_BACKGROUND);
        }

        if (piece == null) {
            System.out.print("-");
        }
        else {
            switch (piece.getColor()) {
                case WHITE:
                    System.out.print(ANSI_YELLOW + piece);
                    break;

                case BLACK:
                    System.out.print(ANSI_PURPLE + piece);
                    break;
            }
        }
        System.out.print(ANSI_RESET + " ");
    }

    private static void printCapturedPieces(ChessPiece[] captured) {
        ChessPiece[] white = new ChessPiece[16];
        ChessPiece[] black = new ChessPiece[16];
        for (ChessPiece c : captured) {
            if (c != null) {
                if (c.getColor() == Color.WHITE) {
                    white[Util.getLength(white)] = c;
                }
                else {
                    black[Util.getLength(black)] = c;
                }
            }
        }

        System.out.println("Captured pieces:");
        System.out.print("White: [" + ANSI_YELLOW);
        for (ChessPiece c : white) {
            if (c != null) {
                System.out.print(c);
            }
        }
        System.out.println(ANSI_RESET + "]");

        System.out.print("Black: [" + ANSI_PURPLE);
        for (ChessPiece c : black) {
            if (c != null) {
                System.out.print(c);
            }
        }
        System.out.println(ANSI_RESET + "]");
        System.out.println();
    }
}
