import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * The Board data type represents a search node as an NxN puzzle, and we will be
 * using it in our Best First Search algorithm to find the optimal solution of
 * an NxN puzzle
 * 
 * @author Saed Mami
 */

public class Board {
    private static final int MIN_BOARD_DIM = 2;
    private int[][] tiles; // N-N array to represent the state of the board
    private int[][] goalTiles; // cashed tiles that represent the solved board
                               // for this board sizes

    /**
     * construct a board from an N-by-N array of blocks (where blocks[i][j] =
     * block in row i, column j)
     *
     * @param inputTiles
     *            N-by-N arrays of blocks that represents a state of the puzzle,
     *            values are from 0 to N^2-1 , where 0 represents the empty
     *            block, the matrix must be squared and N must be >= 8, repeated
     *            Entries will not be checked
     * 
     * @throws IllegalArgumentException
     *             If the passed array is not squared
     * @throws NullPointerExeption
     *             If passed a null array
     * 
     */
    public Board(int[][] inputTiles) {
        if (!IsInputValid(inputTiles))
            throw new IllegalArgumentException();
        tiles = inputTiles;

        // construct and cache the goal board tiles to test for solved state
        // later
        goalTiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles.length; ++j) {
                goalTiles[i][j] = tiles.length * i + j + 1;
            }
        }
        goalTiles[tiles.length - 1][tiles.length - 1] = 0; // the last(empty)
                                                           // tile is
                                                           // represented by 0
    }

    // checks board validity from size perspective
    private boolean IsInputValid(int[][] input) {
        if (input == null)
            throw new NullPointerException();
        int rows = input.length;
        if (rows < MIN_BOARD_DIM)
            return false; // board is too small
        int cols = input[0].length;
        if (cols != rows)
            return false; // columns and rows numbers must match

        return true;
    }

    /**
     * The dimension of the board
     * 
     * @return N, the board dimension
     */
    public int dimension() {
        return tiles.length;
    }

    /**
     * number of blocks out of place, this will help compute the Hamming cost
     * function when we try to solve the puzzle
     * 
     * @return the number of blocks out of place comparing to the goal board
     */
    public int hamming() {
        int outOfPlace = 0;
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles.length; ++j) {
                if (i == tiles.length - 1 && j == i)
                    continue; // ignore the empty block
                if (tiles[i][j] != goalTiles[i][j]) {
                    outOfPlace++;
                }
            }
        }
        return outOfPlace;
    }

    /**
     * sum of Manhattan distances between blocks and goal, this will help
     * compute the Manhattan cost function when we try to solve the puzzle
     * 
     * @return the sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        int manhattanDistances = 0;
        int N = tiles.length;

        // for every tile position, read its value and determine which (row,
        // column) it should be at to satisfy the goal board, and then compare
        // that
        // calculated goal position with the actual position to get the
        // difference and keep summing the differences
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0) // ignore the empty tile
                    continue;
                int goali = (tiles[i][j] - 1) / N;
                int goalj = (tiles[i][j] - 1) % N;
                manhattanDistances += Math.abs(i - goali) + Math.abs(j - goalj);
            }
        }
        return manhattanDistances;
    }

    /**
     * Is this board the goal board
     * 
     * @return true if this board is the goal board, i.e. is it solved?
     */
    public boolean isGoal() {
        return Arrays.deepEquals(tiles, goalTiles);
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     * 
     * @return A board object that represent the "twin", ie obtained by swapping
     *         any two pair in the original board
     */
    public Board twin() {
        // perform deep copy of our tiles array
        int[][] twinTiles = copyTiles();

        // we can obtain the twin board by swapping two arbitrary blocks, empty
        // is not considered a block

        // find the empty block and then, exchange the blocks right and down to
        // it
        int iempty = 0, jempty = 0;
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles.length; ++j) {
                if (twinTiles[i][j] == 0) {
                    iempty = i;
                    jempty = j;
                }
            }
        }

        // exchange two blocks, one by moving to the right of empty block, and
        // one by moving down the empty block
        int iBlock1 = (iempty + 1) % tiles.length;
        int jBlock2 = (jempty + 1) % tiles.length;

        int temp = twinTiles[iBlock1][jempty];
        twinTiles[iBlock1][jempty] = twinTiles[iempty][jBlock2];
        twinTiles[iempty][jBlock2] = temp;

        return new Board(twinTiles);
    }

    // returns a deep copy of the tiles array
    private int[][] copyTiles() {
        int[][] copiedTiles = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++)
            copiedTiles[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        return copiedTiles;
    }

    /**
     * Does this board equal y?
     * 
     * @return true if this board is equal to passed board
     */
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;
        Board other = (Board) y;
        int[][] otherTiles = other.tiles;
        return Arrays.deepEquals(tiles, otherTiles);
    }

    /**
     * All the neighbor boards to the current board, a neighbor board can be
     * obtained by making a play move on the current board, by sliding the empty
     * in any allowed direction
     * 
     * @return a container to all the neighbor boards of the this board
     */
    public Iterable<Board> neighbors() {
        // first we find the indices of the empty block
        int iempty = -1;
        int jempty = -1;
        int N = tiles.length;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0) {
                    iempty = i;
                    jempty = j;
                }
            }
        }

        // we can find the neighbors by swapping the value of the empty block by
        // either the right, left, up and down tiles

        // the 1D indices of tiles neighboring the empty block
        int[] neighborTiles = new int[4];
        neighborTiles[0] = convert2DIndexTo1D(iempty, jempty - 1); // left
        neighborTiles[1] = convert2DIndexTo1D(iempty, jempty + 1); // right
        neighborTiles[2] = convert2DIndexTo1D(iempty - 1, jempty); // up
        neighborTiles[3] = convert2DIndexTo1D(iempty + 1, jempty); // down

        Queue<Board> neighbors = new Queue<Board>();

        // if the neighbor has valid indices evaluate the board add it to the
        // queue
        for (int i = 0; i < neighborTiles.length; ++i) {
            if (neighborTiles[i] == -1)
                continue; // that means this particular neighbor is out of
                          // bounds
            Board neighborBoard = getNeighborBoard(neighborTiles[i], iempty, jempty);
            neighbors.enqueue(neighborBoard);
        }

        return neighbors;
    }

    // converts column, row indexing into a 1D based index, returns -1 if row or
    // column are out of bounds
    private int convert2DIndexTo1D(int i, int j) {
        int N = tiles.length;
        if (i < 0 || i >= N || j < 0 || j >= N) {
            return -1; // out of bounds;
        }
        int index = i * tiles.length + j;
        return index;
    }

    // returns a neighbor board by swapping the empty element, by the tiles at
    // tileIndexToSwap
    private Board getNeighborBoard(int tileIndexToSwap /* 1D */, int emptyRow, int emptyCol) {
        int N = tiles.length;

        int i_neighbor = tileIndexToSwap / N;
        int j_neighbor = tileIndexToSwap % N;

        int[][] neighborTiles = copyTiles();
        neighborTiles[emptyRow][emptyCol] = tiles[i_neighbor][j_neighbor];
        neighborTiles[i_neighbor][j_neighbor] = 0;

        return new Board(neighborTiles);
    }

    /**
     * string representation of this board
     * 
     * @return a string representing the current state of the board. example: 3
     *         1 0 3 4 2 5 7 8 6
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = tiles.length;
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // generate a goal board and test that isGoal() returns true
        int[][] goalTiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board goalBoard = new Board(goalTiles);
        boolean isGoal = goalBoard.isGoal();
        assert(isGoal);
        StdOut.println("testing goal board\n");
        StdOut.print(goalBoard.toString());

        // mahattan and hamming should return 0 for goal board

        int goalMan = goalBoard.manhattan();
        System.out.format("Manhattan cost for the goal board is %d \n", goalMan);

        int goalHam = goalBoard.hamming();
        System.out.format("hamming cost for the goal board is %d \n", goalHam);

        // test various functions for an arbitrary board
        System.out.println("testing a non-goal board");
        int[][] tiles1 = { { 2, 4, 0 }, { 5, 7, 8 }, { 3, 6, 1 } };
        Board board1 = new Board(tiles1);
        System.out.println("test board is");
        System.out.print(board1);
        boolean isGoal1 = board1.isGoal();
        assert(!isGoal);

        // print the neighbors
        System.out.println("the board neighbours are : ");
        for (Board neighbor : board1.neighbors()) {
            System.out.print(neighbor.toString());
        }

        // test hamming and manhattan scores for the board
        System.out.format("hamming cost for the board is %d\n", board1.hamming());
        System.out.format("manhattan cost for the board is %d\n", board1.manhattan());

        // print the twin
        System.out.println("the twin board is : ");
        System.out.print(board1.twin().toString());

    }
}
