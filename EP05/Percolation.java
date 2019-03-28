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

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid; // Represent numbers for the percolation
    private int[][] dir; // 4 directions
    private int maxn; // Size of percolation
    private int cntOpen; // Number of opened cells

    private int auxT; // Auxiliates in counting
    private int auxB;

    private boolean[][] gridOpen; // Tell if a position is open

    private WeightedQuickUnionUF ufp; // Union find for percolation

    // Creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){

        if (n <= 0)
            throw new IllegalArgumentException();

        maxn = n;
        // grid[0][0] == Upper-left site
        grid = new int[n][n];
        gridOpen = new boolean[n][n];

        // Giving each cell of the grid an unique value, for the UF
        int cnt = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = cnt++;

        // [n*n] represents the flow from up
        // [n*n + 1] represents down flow
        ufp = new WeightedQuickUnionUF((n*n) + 2);

        // Setting directions
        dir = new int[][]{ {1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        auxT = auxB = 1;
    }

    // Opens the site (row, col) if it is not open already
    public void open(int row, int col){

        if (row < 0 || row >= maxn || col < 0 || col >= maxn)
            throw new IllegalArgumentException();

        // If had already beeing opened
        if (gridOpen[row][col]) return;

        // Opening it
        gridOpen[row][col] = true;
        cntOpen++;

        // Visiting and joining 4 adjacent cells
        for (int i = 0; i < 4; i++){

            int nrow = row + dir[i][0];
            int ncol = col + dir[i][1];

            // Out of bounderyes
            if (nrow < 0 || ncol < 0 || nrow >= maxn || ncol >= maxn)
                continue;

            // Cell not opened yet
            if (!gridOpen[nrow][ncol])
                continue;

            // Joing them
            ufp.union(grid[nrow][ncol], grid[row][col]);
        }

        // Connects to the flow
        if (row == 0){
            ufp.union(grid[row][col], maxn*maxn);

            if (auxT > 0) auxT = 0;
        }

        // Cheking if there is a percolation
        else if (row == (maxn - 1)){
            ufp.union(grid[row][col], maxn*maxn + 1);

            if (auxB > 0) auxB = 0;
        }
    }

    // Is the site (row, col) open?
    public boolean isOpen(int row, int col){

        if (row < 0 || row >= maxn || col < 0 || col >= maxn)
            throw new IllegalArgumentException();

        return gridOpen[row][col];
    }

    // Is the site (row, col) full?
    public boolean isFull(int row, int col){

        if (row < 0 || row >= maxn || col < 0 || col >= maxn)
            throw new IllegalArgumentException();

        return ufp.connected(grid[row][col], maxn*maxn);
    }

    // Returns the number of open sites
    public int numberOfOpenSites(){
        return cntOpen;
    }

    // Does the system percolate?
    public boolean percolates(){
        return ufp.connected(maxn*maxn, maxn*maxn + 1);
    }

    // Unit testing (required)
    public static void main(String[] args){

        // javac-algs4 Percolation.java && java-algs4 Percolation

        // Creating percolation with n == 6 for unit testing
        Percolation pc = new Percolation(6);

        StdOut.println("\nTamanho da percolation criada: 6x6\n"
            + "Digite '0' para sair do teste\n"
            + "Digite 'OP i j' para abrir a celula [i][j]\n"
            + "Digite 'ISO i j' para ver se a celula [i][j] esta aberta\n"
            + "Digite 'IF i j' para ver se a celula [i][j] esta cheia\n"
            + "Digite 'N' para saber o numero de campos abertos\n"
            + "Digite 'P' para saber se o sistema 'percolates'.\n");

        while (true){
            String op = StdIn.readString();

            if (op.equals("0")){
                break;
            }

            else if (op.equals("OP")){
                int i = StdIn.readInt();
                int j = StdIn.readInt();

                pc.open(i, j);
            }

            else if (op.equals("ISO")){
                int i = StdIn.readInt();
                int j = StdIn.readInt();

                if (pc.isOpen(i, j)) StdOut.println("Esta aberta");
                else StdOut.println("Esta fechada");
            }

            else if (op.equals("IF")){
                int i = StdIn.readInt();
                int j = StdIn.readInt();

                if (pc.isFull(i, j)) StdOut.println("Esta cheia");
                else StdOut.println("Não esta cheia");
            }

            else if (op.equals("N")){
                StdOut.println(pc.numberOfOpenSites());
            }

            else if (op.equals("P")){
                if (pc.percolates()) StdOut.println("Percola");
                else StdOut.println("Não percola");
            }
        }
    }
}
