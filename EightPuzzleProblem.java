import java.util.*;

class PuzzleState {
    int[] board;
    int emptyIndex;
    String path;

    public PuzzleState(int[] board, int emptyIndex, String path) {
        this.board = board;
        this.emptyIndex = emptyIndex;
        this.path = path;
    }

    public boolean isGoal(int[] goal) {
        return Arrays.equals(board, goal);
    }

    public List<PuzzleState> getNeighbors() {
        List<PuzzleState> neighbors = new ArrayList<>();
        int[] directions = {-3, 3, -1, 1};
        int x = emptyIndex / 3;
        int y = emptyIndex % 3;

        for (int direction : directions) {
            int newIndex = emptyIndex + direction;

            if (direction == -1 && y == 0) continue;
            if (direction == 1 && y == 2) continue;
            if (newIndex < 0 || newIndex >= 9) continue;

            if (Math.abs(emptyIndex - newIndex) == 3 || Math.abs(emptyIndex - newIndex) == 1) {
                int[] newBoard = board.clone();
                newBoard[emptyIndex] = newBoard[newIndex];
                newBoard[newIndex] = 0;
                neighbors.add(new PuzzleState(newBoard, newIndex, path + (direction == -3 ? "Up " : direction == 3 ? "Down " : direction == -1 ? "Left " : "Right ")));
            }
        }
        return neighbors;
    }

    public int heuristic(int[] goal) {
        int distance = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] != 0) {
                int goalIndex = findIndex(goal, board[i]);
                int currentX = i / 3;
                int currentY = i % 3;
                int goalX = goalIndex / 3;
                int goalY = goalIndex % 3;
                distance += Math.abs(currentX - goalX) + Math.abs(currentY - goalY);
            }
        }
        return distance;
    }

    private int findIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PuzzleState that = (PuzzleState) obj;
        return Arrays.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}

class PuzzleSolver {
    public static void solvePuzzle(int[] initial, int[] goal) {
        PriorityQueue<PuzzleState> openList = new PriorityQueue<>(Comparator.comparingInt(s -> s.heuristic(goal)));
        Set<PuzzleState> closedList = new HashSet<>();

        int emptyIndex = findEmptyIndex(initial);
        PuzzleState startState = new PuzzleState(initial, emptyIndex, "");

        openList.add(startState);

        while (!openList.isEmpty()) {
            PuzzleState current = openList.poll();

            if (current.isGoal(goal)) {
                System.out.println("Solution path: " + current.path);
                printBoard(current.board);
                return;
            }

            closedList.add(current);

            for (PuzzleState neighbor : current.getNeighbors()) {
                if (closedList.contains(neighbor)) continue;
                openList.add(neighbor);
            }
        }
        System.out.println("No solution found.");
    }

    private static int findEmptyIndex(int[] board) {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }

    public static void printBoard(int[] board) {
        System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2]);
        System.out.println("---|---|---");
        System.out.println(" " + board[3] + " | " + board[4] + " | " + board[5]);
        System.out.println("---|---|---");
        System.out.println(" " + board[6] + " | " + board[7] + " | " + board[8]);
        System.out.println("---|---|---");
    }
}

public class EightPuzzleProblem {
    public static void main(String[] args) {
        int[] initial = new int[9];
        int[] goal = new int[9];

        Scanner sc = new Scanner(System.in);
        System.out.print("\n\n Enter Details of Initial State \n\n");
        for (int i = 0; i < 9; i++) {
            System.out.print("\nInitial[" + (i + 1) + "] : ");
            initial[i] = sc.nextInt();
        }

        System.out.print("\n\n Enter Details of Goal State \n\n");
        for (int i = 0; i < 9; i++) {
            System.out.print("\nGoal[" + (i + 1) + "] : ");
            goal[i] = sc.nextInt();
        }

        System.out.print("\n\n ---------- Initial State ----------- \n\n");
        PuzzleSolver.printBoard(initial);

        System.out.print("\n\n ---------- Goal State ----------- \n\n");
        PuzzleSolver.printBoard(goal);

        System.out.println("\nSolving Puzzle...");
        PuzzleSolver.solvePuzzle(initial, goal);
    }
}
