import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Solver for the N-puzzle problem using best first search (A*) algorithm
 * Compilation: javac Solver.java Execution: java CollisionSystem Solver
 * board.txt board.txt : (a text file the describes the board) Dependencies:
 * algs4.In.java, algs4.MinPQ.java, algs4.Stack.java, algs4.StdOut.java
 * Board.java
 * 
 * @author Saed Mami
 */

public class Solver {
    private MinPQ<SearchNode> searchNodes; // search nodes of the A* algorithm,
                                           // maintained by least priority
    private MinPQ<SearchNode> twinSearchNodes; // only to determine if the board
                                               // is solvable or not
    private boolean isSolvable = true; // always be optimistic

    /**
     * Solves the N puzzle problem on an NxN board by running the A* algorithm
     * on the initial board, in order to determine the solvability of the board,
     * we also run the algorithm in parallel for the board twin, it has been
     * proved that for a for a board and it's twin, only one of them will be
     * solvable.
     */
    public Solver(Board initial) {
        // run the A* algorithm in parallel for original and twin board
        SearchNode initialNode = new SearchNode(initial, null);
        SearchNode initialTwinNode = new SearchNode(initial.twin(), null);

        searchNodes = new MinPQ<SearchNode>(initialNode.new ByHamming());
        twinSearchNodes = new MinPQ<SearchNode>(initialTwinNode.new ByHamming());

        searchNodes.insert(initialNode);
        twinSearchNodes.insert(initialTwinNode);

        while (!searchNodes.min().board.isGoal()) {
            if (twinSearchNodes.min().board.isGoal()) {
                isSolvable = false; // if the twin board gets solved that means
                                    // that the original board is unsolvable
                break;
            }

            SearchNode currentNode = searchNodes.delMin();
            SearchNode parentNode = currentNode.previousNode;

            SearchNode currentTwinNode = twinSearchNodes.delMin();
            SearchNode parentTwinNode = currentTwinNode.previousNode;

            for (Board child : currentNode.board.neighbors()) {
                if (parentNode != null && child.equals(parentNode.board))
                    continue;
                searchNodes.insert(new SearchNode(child, currentNode));
            }

            for (Board child : currentTwinNode.board.neighbors()) {
                if (parentTwinNode != null && child.equals(parentTwinNode.board))
                    continue;
                twinSearchNodes.insert(new SearchNode(child, currentTwinNode));
            }
        }
    }

    // inner class to represent search nodes inserted in the priority queue
    private class SearchNode {
        Board board;
        SearchNode previousNode; // the board in the previous search step
                                 // (parent)
        int nodeDepth; // records the number of moves made to reach this search
                       // node

        public SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previousNode = previous;
            if (previous == null)
                nodeDepth = 0;
            else
                nodeDepth = previous.nodeDepth + 1;
        }

        // a comparator for the manhattan priority function
        private class ByManhattan implements Comparator<SearchNode> {
            @Override
            public int compare(SearchNode node1, SearchNode node2) {
                int cost1 = node1.nodeDepth + node1.board.manhattan();
                int cost2 = node2.nodeDepth + node2.board.manhattan();
                return cost1 - cost2;
            }
        }

        // a comparator for the hamming priority function
        public class ByHamming implements Comparator<SearchNode> {
            @Override
            public int compare(SearchNode node1, SearchNode node2) {
                int cost1 = node1.nodeDepth + node1.board.hamming();
                int cost2 = node2.nodeDepth + node2.board.hamming();
                return cost1 - cost2;
            }
        }
    }

    /**
     * is the initial board solvable?
     * 
     * @return a boolean that indicates whether the initial board given is
     *         solvable or not
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * number of moves
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (isSolvable())
            return searchNodes.min().nodeDepth;
        else
            return -1;
    }

    /**
     * The initial board solution
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable)
            return null;
        SearchNode lastNode = searchNodes.min();
        SearchNode step = lastNode;
        Stack<Board> solution = new Stack<Board>();
        while (step != null) {
            solution.push(step.board);
            step = step.previousNode;
        }
        
        return solution;
    }
    

    /**
     * solves an NxN puzzle from a file given as a parameter, the first thing
     * read from the file is dimension of the board followed by the tile values,
     * empty tile is given a value of 0
     * 
     * @param A
     *            text file that has the puzzle board description
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // int[][] tiles = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        // Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
