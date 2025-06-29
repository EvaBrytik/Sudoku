import java.util.*;

public class Main {
    static final int SIZE = 9;

    static final String WHITE = "\033[97m";
    static final String GREEN = "\033[92m";
    static final String RED = "\033[91m";
    static final String CYAN = "\033[96m";
    static final String RESET = "\033[0m";

    static int attemptCount = 0;
    static int backtrackCount = 0;
    static long solveStart, solveEnd;
    static final Random rand = new Random();

    static int[][] board = new int[SIZE][SIZE];
    static int[][] solutionBoard = new int[SIZE][SIZE];
    static boolean[][] isOriginalCell = new boolean[SIZE][SIZE];
    static Scanner sc = new Scanner(System.in);

    //========================= Screen Control =========================//
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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

    public static boolean isBoardValid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int num = board[row][col];
                if (num == 0) continue;
                board[row][col] = 0;
                if (!isValid(row, col, num)) {
                    board[row][col] = num;
                    return false;
                }
                board[row][col] = num;
            }
        }
        return true;
    }

    //========================= Sudoku Solver =========================//
    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            attemptCount++;
                            if (solveSudoku(board)) return true;
                            board[row][col] = 0;
                            backtrackCount++;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    //========================= Display Board =========================//
    public static void displayBoard(int[][] board, boolean[][] isOriginalCell) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");

                if (board[i][j] == 0) {
                    System.out.print(". ");
                } else if (isOriginalCell[i][j]) {
                    System.out.print(WHITE + board[i][j] + " " + RESET);
                } else if (board[i][j] < 0) {
                    System.out.print(RED + (-board[i][j]) + " " + RESET);
                } else {
                    System.out.print(GREEN + board[i][j] + " " + RESET);
                }
            }
            System.out.println();
        }
    }

    //========================= Manual Input =========================//
    public static void inputBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                while (true) {
                    clearScreen();
                    displayBoard(board, isOriginalCell);
                    System.out.print("Enter value for cell (" + (i + 1) + "," + (j + 1) + "): ");
                    String input = sc.nextLine().trim();
                    if (input.isEmpty()) {
                        board[i][j] = 0;
                        isOriginalCell[i][j] = false;
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

    //========================= Final Output =========================//
    public static void displayFinalBoard() {
        clearScreen();
        System.out.println("Solved Board:\n");
        displayBoard(board, isOriginalCell);
        System.out.println("\nBrute-force attempts: " + attemptCount);
        System.out.println("Backtracks: " + backtrackCount);
        System.out.println("Solve time: " + (solveEnd - solveStart) + " ms");
    }

    //========================= Generator =========================//
    public static void generateFullBoard() {
        int[][] newBoard = new int[SIZE][SIZE];
        fillBoard(newBoard, 0, 0);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = newBoard[i][j];
                solutionBoard[i][j] = newBoard[i][j];
            }
        }
    }

    public static boolean fillBoard(int[][] board, int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return fillBoard(board, row + 1, 0);

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) numbers.add(i);
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (isValidForBoard(board, row, col, num)) {
                board[row][col] = num;
                if (fillBoard(board, row, col + 1)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    public static boolean isValidForBoard(int[][] targetBoard, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (targetBoard[row][i] == num || targetBoard[i][col] == num) return false;
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (targetBoard[startRow + i][startCol + j] == num) return false;
            }
        }
        return true;
    }

    //========================= Puzzle Masking =========================//
    public static void maskBoard(int difficulty) {
        double revealChance = 0.7 - ((difficulty - 1) * 0.06);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (rand.nextDouble() < revealChance) {
                    board[i][j] = solutionBoard[i][j];
                    isOriginalCell[i][j] = true;
                } else {
                    board[i][j] = 0;
                    isOriginalCell[i][j] = false;
                }
            }
        }
    }

    //========================= Gameplay =========================//
    public static void playGeneratedPuzzle() {
        long start = System.currentTimeMillis();
        while (!isBoardComplete()) {
            clearScreen();
            displayBoard(board, isOriginalCell);
            System.out.print("Enter Y (1-9) or 0 to quit: ");
            int x = Integer.parseInt(sc.nextLine()) - 1;
            if (x == -1) return;

            System.out.print("Enter X (1-9): ");
            int y = Integer.parseInt(sc.nextLine()) - 1;

            if (!isInBounds(x, y)) continue;

            if (isOriginalCell[x][y]) {
                System.out.println("That cell is permanent. Press Enter to continue.");
                sc.nextLine();
                continue;
            }

            System.out.print("Enter guess (1-9), or 'cancel' to go back: ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) continue;
            if (!input.matches("[1-9]")) continue;

            int guess = Integer.parseInt(input);
            board[x][y] = (guess == solutionBoard[x][y]) ? guess : -guess;
        }
        clearScreen();
        displayBoard(board, isOriginalCell);
        long end = System.currentTimeMillis();
        System.out.println("\nCongratulations! You solved the puzzle.");
        System.out.println("Time taken: " + (end - start) / 1000.0 + " seconds");
    }

    //========================= Helpers =========================//
    public static boolean isInBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public static boolean isBoardComplete() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] <= 0) return false;
            }
        }
        return true;
    }

    //========================= Menu Loop =========================//
    public static void main(String[] args) {
        while (true) {
            clearScreen();
            System.out.println("===== SUDOKU MENU =====");
            System.out.println("[1] Solve a Sudoku");
            System.out.println("[2] Generate a Sudoku");
            System.out.println("[3] Quit");
            System.out.print("Select an option: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                board = new int[SIZE][SIZE];
                isOriginalCell = new boolean[SIZE][SIZE];
                attemptCount = 0;
                backtrackCount = 0;
                inputBoard();

                if (!isBoardValid()) {
                    System.out.println("Your board has invalid placements. Please check for duplicates.");
                    System.out.println("Press Enter to return to the menu...");
                    sc.nextLine();
                    continue;
                }

                solveStart = System.currentTimeMillis();
                boolean solved = solveSudoku(board);
                solveEnd = System.currentTimeMillis();

                if (solved) {
                    displayFinalBoard();
                    System.out.println("\nPress Enter to return to menu...");
                    sc.nextLine();
                } else {
                    System.out.println("No solution exists.");
                    System.out.println("Press Enter to return to menu...");
                    sc.nextLine();
                }
            } else if (choice.equals("2")) {
                
                generateFullBoard();
                System.out.print("Select difficulty (1–10): ");
                int diff = Math.max(1, Math.min(10, sc.nextInt()));
                sc.nextLine();
                maskBoard(diff);
                playGeneratedPuzzle();
                System.out.println("\nPress Enter to return to menu...");
                sc.nextLine();
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
