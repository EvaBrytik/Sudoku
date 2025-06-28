import java.util.*;

public class GenerateSudoku {
    static final int SIZE = 9;
    static final String WHITE = "\033[97m";
    static final String GREEN = "\033[92m";
    static final String RED = "\033[91m";
    static final String RESET = "\033[0m";

    static int[][] board = new int[SIZE][SIZE];
    static int[][] solutionBoard = new int[SIZE][SIZE];
    static boolean[][] isOriginalCell = new boolean[SIZE][SIZE];
    static Scanner sc = new Scanner(System.in);

    //========================= Gameplay Entry =========================//
    public static void playGeneratedPuzzle() {
        generateFullBoard();
        System.out.print("Select difficulty (1-10): ");
        int difficulty = Math.max(1, Math.min(10, sc.nextInt()));
        sc.nextLine();
        maskBoard(difficulty);

        long startTime = System.currentTimeMillis();
        while (!isBoardComplete()) {
            displayBoard();
            System.out.print("Enter Y (1-9) or 0 to quit: ");
            int x = Integer.parseInt(sc.nextLine()) - 1;
            if (x == -1) return;
            //long story, just keep the y as x, and the x as y.. it works
            System.out.print("Enter X (1-9): ");
            int y = Integer.parseInt(sc.nextLine()) - 1;

            if (isOriginalCell[x][y]) {
                System.out.println("You can't change a pre-filled cell. Press Enter to continue.");
                sc.nextLine();
                continue;
            }

            System.out.print("Enter guess (1-9) or 'cancel': ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) continue;
            if (!input.matches("[1-9]")) continue;

            int guess = Integer.parseInt(input);
            board[x][y] = (guess == solutionBoard[x][y]) ? guess : -guess;
        }

        displayBoard();
        long endTime = System.currentTimeMillis();
        System.out.println("\nCongratulations! You solved it in " + (endTime - startTime) / 1000.0 + " seconds");
    }

    //========================= Board Generation =========================//
    public static void generateFullBoard() {
        fillBoard(board, 0, 0);
        for (int i = 0; i < SIZE; i++) {
            solutionBoard[i] = Arrays.copyOf(board[i], SIZE);
        }
    }

    public static boolean fillBoard(int[][] board, int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return fillBoard(board, row + 1, 0);

        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) nums.add(i);
        Collections.shuffle(nums);

        for (int num : nums) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (fillBoard(board, row, col + 1)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    public static boolean isSafe(int[][] board, int row, int col, int num) {
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

    //========================= Puzzle Masking =========================//
    public static void maskBoard(int difficulty) {
        double revealChance = 0.7 - ((difficulty - 1) * 0.06);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (Math.random() < revealChance) {
                    board[i][j] = solutionBoard[i][j];
                    isOriginalCell[i][j] = true;
                } else {
                    board[i][j] = 0;
                    isOriginalCell[i][j] = false;
                }
            }
        }
    }

    //========================= Completion Check =========================//
    public static boolean isBoardComplete() {
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
                if (isOriginalCell[i][j]) System.out.print(WHITE + board[i][j] + " " + RESET);
                else if (board[i][j] > 0) System.out.print(GREEN + board[i][j] + " " + RESET);
                else if (board[i][j] < 0) System.out.print(RED + (-board[i][j]) + " " + RESET);
                else System.out.print(". ");
            }
            System.out.println();
        }
    }
}
