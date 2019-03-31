/****************************************************************
    Nome: Felipe Castro de Noronha
    NUSP: 10737032

    Ao preencher esse cabeçalho com o meu nome e o meu número USP,
    declaro que todas as partes originais desse exercício programa (EP)
    foram desenvolvidas e implementadas por mim e que portanto não
    constituem desonestidade acadêmica ou plágio.
    Declaro também que sou responsável por todas as cópias desse
    programa e que não distribui ou facilitei a sua distribuição.
    Estou ciente que os casos de plágio e desonestidade acadêmica
    serão tratados segundo os critérios divulgados na página da
    disciplina.
    Entendo que EPs sem assinatura devem receber nota zero e, ainda
    assim, poderão ser punidos por desonestidade acadêmica.

    Abaixo descreva qualquer ajuda que você recebeu para fazer este
    EP.  Inclua qualquer ajuda recebida por pessoas (inclusive
    monitoras e colegas). Com exceção de material de MAC0323, caso
    você tenha utilizado alguma informação, trecho de código,...
    indique esse fato abaixo para que o seu programa não seja
    considerado plágio ou irregular.

    Descrição de ajuda ou indicação de fonte:

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class Solver {

    /*
        Solver for the variation of the game 8puzzle. Solve a given board with
        A* searchign algorithm.

        Problem taks can be found at:
        http://www.cs.princeton.edu/courses/archive/spring19/cos226/assignments/8puzzle/specification.php
    */

    private Queue<Board> ans; // Queue that store the boards antil resulition
    private int nMoves; // Numbem of moves needed to solve

    // Find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){

        // Dealing with invalid input
        if (initial == null || !initial.isSolvable())
            throw new java.lang.IllegalArgumentException();

        // Minimal priority queue used in A* seach
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        ans = new Queue<Board>();

        // Initial SearchNode, with initial board, 0 moves and null preview board
        SearchNode sn = new SearchNode(initial, 0, null);
        pq.insert(sn);

        // Loop to find solution
        while (!pq.isEmpty()) {

            // Getting Minimal element from PQ
            SearchNode aux = pq.min();
            pq.delMin();
            // Storing it in ans queue
            ans.enqueue(aux.crt());

            // If i'm in a solution
            if (aux.crt().isGoal()){
                nMoves = aux.nm();
                break;
            }

            // Iterating through all possible moves from actual board
            for (Board u : aux.crt().neighboors()){

                // Optimization
                if (u.equals(aux.prv())) continue;

                // Initializing seach node that will be stored in PQ
                SearchNode ist = new SearchNode(u, aux.nm()+1, aux.crt());
                pq.insert(ist);
            }
        }
    }

    // Class responsable for SearchNode and comparations
    private class SearchNode implements Comparable<SearchNode>{

        private Board current; // Current board of this node
        private int numberMoves; // Number of moves until now
        private Board prev; // Board from with this node come from

        // Constructor
        public SearchNode(Board c, int n, Board p) {

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
        public Board prv() {
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
        return ans;
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
