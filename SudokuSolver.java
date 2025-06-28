import java.util.Scanner;

public class SudokuSolver {
    static final int SIZE = 9;
    static final String WHITE = "\033[97m";
    static final String GREEN = "\033[92m";
    static final String RESET = "\033[0m";

    static int[][] board = new int[SIZE][SIZE];
    static boolean[][] isOriginalCell = new boolean[SIZE][SIZE];
    static Scanner sc = new Scanner(System.in);

    //========================= Entry Point =========================//
    public static void playSolver() {
        inputBoard();
        long start = System.currentTimeMillis();
        boolean solved = solveSudoku();
        long end = System.currentTimeMillis();

        if (solved) {
            displayFinalBoard();
            System.out.println("\nSolve time: " + (end - start) + " ms");
        } else {
            System.out.println("No solution exists.");
        }

        System.out.println("\nPress Enter to return to menu...");
        sc.nextLine();
    }

    //========================= User Input =========================//
    public static void inputBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                while (true) {
                    displayBoard(i, j);
                    System.out.print("Enter value for cell (" + (i + 1) + "," + (j + 1) + "): ");
                    String input = sc.nextLine().trim();

                    if (input.isEmpty()) {
                        board[i][j] = 0;
                        break;
                    }
                    if (input.matches("[1-9]")) {
                        board[i][j] = Integer.parseInt(input);
                        isOriginalCell[i][j] = true;
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter 1–9 or blank.");
                    }
                }
            }
        }
    }

    //========================= Board Display =========================//
    public static void displayBoard(int cursorRow, int cursorCol) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");

                if (i == cursorRow && j == cursorCol) System.out.print("X ");
                else if (board[i][j] == 0) System.out.print(". ");
                else System.out.print(WHITE + board[i][j] + " " + RESET);
            }
            System.out.println();
        }
    }

    //========================= Backtracking Solver =========================//
    public static boolean solveSudoku() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku()) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    //========================= Validity Check =========================//
    public static boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) return false;
            }
        }
        return true;
    }

    //========================= Final Display =========================//
    public static void displayFinalBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Solved Board:");

        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");

                System.out.print((isOriginalCell[i][j] ? WHITE : GREEN) + board[i][j] + " " + RESET);
            }
            System.out.println();
        }
    }
}
