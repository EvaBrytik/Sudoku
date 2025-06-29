
import java.util.*;

public class GenerateSudoku {
    static final int SIZE = 9;
    static final String WHITE = "\033[97m";
    static final String GREEN = "\033[92m";
    static final String RED = "\033[91m";
    static final String RESET = "\033[0m";
    static int[][] board = new int[SIZE][SIZE];
    static int[][] solution = new int[SIZE][SIZE];
    static boolean[][] isOriginal = new boolean[SIZE][SIZE];
    static Scanner sc = new Scanner(System.in);

    //========================= Entry Point =========================//
    public static void playGeneratedPuzzle() {
        generateFullBoard();
        System.out.print("Select difficulty (1-10): ");
        int difficulty = Math.max(1, Math.min(10, sc.nextInt()));
        sc.nextLine();
        maskBoard(difficulty);

        long startTime = System.currentTimeMillis();
        while (!isSolved()) {
            displayBoard();
            System.out.print("Enter Y (1-9) or 0 to quit: ");
            int x = Integer.parseInt(sc.nextLine()) - 1;
            if (x == -1) return;
            System.out.print("Enter X (1-9): ");
            int y = Integer.parseInt(sc.nextLine()) - 1;

            if (isOriginal[x][y]) {
                System.out.println("You can't change a pre-filled cell. Press Enter to continue.");
                sc.nextLine();
                continue;
            }

            System.out.print("Enter guess (1-9) or 'cancel': ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) continue;
            if (!input.matches("[1-9]")) continue;

            int guess = Integer.parseInt(input);
            board[x][y] = (guess == solution[x][y]) ? guess : -guess;
        }

        displayBoard();
        long endTime = System.currentTimeMillis();
        System.out.println("\nCongratulations! You solved it in " + (endTime - startTime) / 1000.0 + " seconds");
    }

    //========================= Generator =========================//
    public static void generateFullBoard() {
        fillBoard(board, 0, 0);
        for (int i = 0; i < SIZE; i++) {
            solution[i] = Arrays.copyOf(board[i], SIZE);
        }
    }

    public static boolean fillBoard(int[][] b, int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return fillBoard(b, row + 1, 0);
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) nums.add(i);
        Collections.shuffle(nums);
        for (int num : nums) {
            if (isSafe(b, row, col, num)) {
                b[row][col] = num;
                if (fillBoard(b, row, col + 1)) return true;
                b[row][col] = 0;
            }
        }
        return false;
    }

    //========================= Validity Check =========================//
    public static boolean isSafe(int[][] b, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (b[row][i] == num || b[i][col] == num) return false;
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b[startRow + i][startCol + j] == num) return false;
            }
        }
        return true;
    }

    //========================= Puzzle Masking =========================//
    public static void maskBoard(int difficulty) {
        double revealChance = 0.7 - ((difficulty - 1) * 0.06);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Math.random() < revealChance) {
                    board[i][j] = solution[i][j];
                    isOriginal[i][j] = true;
                } else {
                    board[i][j] = 0;
                    isOriginal[i][j] = false;
                }
            }
        }
    }

    //========================= Helpers =========================//
    public static boolean isSolved() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] <= 0) return false;
            }
        }
        return true;
    }

    //========================= Board Display =========================//
    public static void displayBoard() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                if (isOriginal[i][j]) System.out.print(WHITE + board[i][j] + " " + RESET);
                else if (board[i][j] > 0) System.out.print(GREEN + board[i][j] + " " + RESET);
                else if (board[i][j] < 0) System.out.print(RED + (-board[i][j]) + " " + RESET);
                else System.out.print(". ");
            }
            System.out.println();
        }
    }
}
