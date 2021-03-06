package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.util.Util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        ChessPiece[] captured = new ChessPiece[31];

        while (!chessMatch.isCheckmate()) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);

                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);

                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
                if (capturedPiece != null) {
                    captured[Util.getLength(captured)] = capturedPiece;
                }

                if (chessMatch.getPromotedPawn() != null) {
                    System.out.print("Choose a promotion option [B/H/R/Q]: ");
                    String piece = sc.nextLine();
                    chessMatch.replacePromotedPawn(piece);
                }

            }
            catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }
}
