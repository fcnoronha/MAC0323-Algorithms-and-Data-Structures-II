import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    /*
        Solver for the variation of the game 8puzzle. Solve a given board with
        A* searchign algorithm.

        Problem taks can be found at:
        http://www.cs.princeton.edu/courses/archive/spring19/cos226/assignments/8puzzle/specification.php
    */

    private SearchNode ans; // Queue that store the boards antil resulition
    private int nMoves; // Numbem of moves needed to solve

    // Find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){

        // Dealing with invalid input
        if (initial == null || !initial.isSolvable())
            throw new java.lang.IllegalArgumentException();

        // Minimal priority queue used in A* seach
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        // Initial SearchNode, with initial board, 0 moves and null preview board
        SearchNode sn = new SearchNode(initial, 0, null);
        pq.insert(sn);

        // Loop to find solution
        while (!pq.isEmpty()) {

            // Getting Minimal element from PQ
            SearchNode aux = pq.min();
            pq.delMin();

            // If i'm in a solution
            if (aux.crt().isGoal()){
                nMoves = aux.nm();
                ans = aux;
                break;
            }

            // Iterating through all possible moves from actual board
            for (Board u : aux.crt().neighbors()){

                // Optimization
                if (aux.prv() != null && u.equals(aux.prv().crt())) continue;

                // Initializing seach node that will be stored in PQ
                SearchNode ist = new SearchNode(u, aux.nm()+1, aux);
                pq.insert(ist);
            }
        }
    }

    // Class responsable for SearchNode and comparations
    private class SearchNode implements Comparable<SearchNode>{

        private Board current; // Current board of this node
        private int numberMoves; // Number of moves until now
        private SearchNode prev; // Board from with this node come from

        // Constructor
        public SearchNode(Board c, int n, SearchNode p) {

            current = c;
            numberMoves = n;
            prev = p;
        }

        // Return priotity of this node
        public int prt(){
            return current.manhattan() + numberMoves;
        }

        // Return number of moves until this node
        public int nm() {
            return numberMoves;
        }

        // Return current board
        public Board crt() {
            return current;
        }

        // Return current board
        public SearchNode prv() {
            return prev;
        }

        // Comparator used in MinPQ
        public int compareTo(SearchNode sn) {
            return (this.prt() - sn.prt());
        }
    }

    // Min number of moves to solve initial board
    public int moves(){
        return nMoves;
    }

    // Sequence of boards in a shortest solution
    public Iterable<Board> solution() {

        Stack<Board> s = new Stack<Board>();
        SearchNode aux = ans;

        // Going back in through solutions
        while (aux != null){
            s.push(aux.crt());
            aux = aux.prv();
        }

        return s;
    }

    // Test client (see below)
    public static void main(String[] args) {

        // Read file passed as argument
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                blocks[row][col] = in.readInt();

        // Initializing board from file
        Board initial = new Board(blocks);

        // Initializing Solver
        Solver solver = new Solver(initial);

        // Number of moves
        StdOut.println("Minimum number of moves = " + solver.moves());

        // Printing solution
        for (Board b : solver.solution())
            StdOut.println(b.toString());
    }
}
