package com.napier.bruno;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * -- Main class. Where the magic happens --
 * @author Bruno Pascaretta Guerra
 */

public class Main {
    /**
     * Setting standards values for each difficulty level.
     */
    static ParSolver easy = new ParSolver("165 794 038 407 002 050 930 006 004 810 405 002 576 239 400 200 601 075 301 507 849 690 000 527 050 028 103");
    static ParSolver medium = new ParSolver("980 254 000 640 073 200 020 000 900 030 000 006 060 000 090 700 642 803 400 026 010 390 008 402 172 000 000");
    static ParSolver hard = new ParSolver("600 500 073 500 002 004 001 003 580 903 080 705 850 037 020 076 050 000 087 300 250 010 470 006 360 821 000");
    static ParSolver insane = new ParSolver("000 970 800 300 005 640 000 060 031 035 040 000 209 007 310 070 030 490 420 006 000 051 304 009 003 000 500");
    static ParSolver usrSudoku;

    /**
     * Main entrypoint to the Sudoku game
     * @param args
     */
    public static void main(String[] args) {
	game();
    }

    /**
     * Interactive menu for the user.
     */
    private static void displayMenu() {
        System.out.println("\n1. Choose coordinates.\n2. Undo.\n3. Give me the solution.\n4. Exit");
    }

    /**
     * Displays the menu for difficulty levels.
     * @return
     */
    private static int difficulty() {
        System.out.println("\n\nWelcome to Bruno's Sudoku\nNow, the game has began.\nChoose a level:\n");
        Scanner scanner = new Scanner(System.in);
        int result = 1;
        System.out.println("\n1. Easy.\n2. Medium. \n3. Hard. \n4. Insane. \n5. Quit game.");
        String userInput = scanner.next().toLowerCase();
        if (userInput.equals("1")) {
            System.out.println(easy.toString());
            usrSudoku = new ParSolver(easy.getBoardAsString());
        }
        else if (userInput.equals("2")) {
            System.out.println(medium.toString());
            usrSudoku = new ParSolver(medium.getBoardAsString());
        }
        else if (userInput.equals("3")) {
            System.out.println(hard.toString());
            usrSudoku = new ParSolver(hard.getBoardAsString());
        }
        else if (userInput.equals("4")) {
            System.out.println(insane.toString());
            usrSudoku = new ParSolver(insane.getBoardAsString());
        }
        else if (userInput.equals("5")) {
            System.out.println("Exiting the game.");
            result = 0;
        }
        else {
            System.out.println("Invalid difficulty selection. Please, try again!");
            result = -1;
        }
        return result;
    }

    /**
     * Parse the value to the board.
     * @param str
     * @return
     */
    private static boolean parseAdd(String str) {
        Scanner scanner = new Scanner(System.in);
        if (str.matches("[A-I][1-9]")) {
            int colASCII = (int)str.charAt(0);
            int rowASCII = (int)str.charAt(1);
            System.out.println("\nNow insert the value.");
            int usrValue = scanner.nextInt();
            if (usrValue <= 9 && usrValue >= 1) {
                if (usrSudoku.add(colASCII - 65, rowASCII - 49, usrValue)) {
                    System.out.println("Successfully added " + usrValue + " to " + str + "\n");
                    System.out.println(usrSudoku.output());
                    return true;
                }
            }
        }
        else {
            return false;
        }
        return false;
    }

    /**
     * Main game run function.
     */
    private static void game() {
        Scanner scanner = new Scanner(System.in);


        boolean endGame;
        int diffReturn;
        String location = "";
        boolean parseAddReturn;

        ArrayList<String> undoList = new ArrayList<String>();
        while ((diffReturn = difficulty()) != 0) {
            endGame = false;
            if (diffReturn == 1) {
                while (!endGame) {
                    displayMenu();
                    String usrChoice = scanner.next();
                    if (usrChoice.equals("1")) {
                        System.out.println("\nType in the location to fill in...");
                        location = scanner.next().toUpperCase();
                        parseAddReturn = parseAdd(location);
                        boolean solvedStatus = usrSudoku.isSolved();
                        if (solvedStatus) {
                            System.out.println("\nYou Won!");
                            endGame = true;
                        }
                        if (!parseAddReturn) {
                            System.out.println("Can't fill this value in this place, try again...");
                            System.out.println(usrSudoku.output());
                        }
                        else {
                            undoList.add(location);
                        }
                    }
                    else if (usrChoice.equals("2")) {
                        int index = undoList.size() - 1;
                        if (index >= 0) {
                            String obj = undoList.get(index);
                            if (obj.matches("[A-I][1-9]")) {
                                usrSudoku.remove((int)obj.charAt(0) - 65, (int)obj.charAt(1) - 49);
                                System.out.println(usrSudoku.output());
                                undoList.remove(index);
                            }
                        }
                        else {
                            System.out.println("Can't undo...");
                        }
                    }
                    else if (usrChoice.equals("3")) {
                        usrSudoku.solve();
                        System.out.println(usrSudoku.toString());
                        endGame = true;
                    }
                    else if (usrChoice.equals("4")) {
                        System.out.println("Quitting to difficulty selection...");
                        endGame = true;
                    }
                    else {
                        System.out.println("Invalid input, try again...");
                        endGame = false;
                    }
                }
            }
        }
    }

}
