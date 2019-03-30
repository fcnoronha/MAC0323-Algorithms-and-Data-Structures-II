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

    1. O algoritimo de contagem de inversoes foi baseado na implementação
    do site GeeksForGeeks, com o link a seguir:
    www.geeksforgeeks.org/java-program-for-count-inversions-in-an-array-set-1-using-merge-sort/

    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/

import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Queue;

public class Board {

    private int[][] tiles; // Represents the grid
    private int n; // Size of one side of the grid

    private GoalPos[] gp; // Auxiliar goal grid

    private int manhattanN;
    private int hammingN;

    private int blankRow; // Row of blank tile (used in isSolvable())

    // Create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {

        // Creating a copy of argumnt matrix
        n = tiles[0].length;
        this.tiles = new int[n][n];
        for (int  i = 0; i < n; i++)
            this.tiles[i] = tiles[i].clone();

        // Setting goal values used in manhattan() and hamming()
        // gp[i] will store the goal (row, col) of the tile with number i
        gp = new GoalPos[n*n];
        for (int i = 0; i < n*n; i++)
            gp[i] = new GoalPos();

        int counter = 1; // Number of actual tile
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){

                // Setting blank tile
                if (i == n-1 && j == n-1)
                    counter = 0;

                gp[counter].setPos(i, j);
                counter++;
            }
        }

        // Counting hamming and manhattans distances
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {

                // If i am in a blank tile
                if (this.tiles[i][j] == 0){
                    blankRow = i;
                    continue;
                }

                // Hamming
                if (!gp[ this.tiles[i][j] ].equals(i, j))
                    hammingN++;

                // Manhattan
                manhattanN += gp[ this.tiles[i][j] ].manhDist(i, j);
            }
        }
    }

    // Used for comparation to the goal board, used as node
    private class GoalPos {

        private int goalRow;
        private int goalCol;

        public GoalPos() {

        }

        // Set goals positions for this node
        public void setPos(int row, int col) {

            goalRow = row;
            goalCol = col;
        }

        // Receive (row, col) and check if it is equal to (goalRow, goalCol)
        public boolean equals(int row, int col) {

            return (row == goalRow && col == goalCol);
        }

        // Receive (row, col) and return the manhattan distance from goals
        public int manhDist(int row, int col) {

            int ret = Math.abs(row - goalRow);
            ret += Math.abs(col - goalCol);

            return ret;
        }
    }

    // String representation of this boarda
    public String toString() {

        String ret = new String();

        ret = String.valueOf(n);
        ret += "\n";

        // Appending each tile to string
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++)
                ret += " " + String.valueOf(tiles[i][j]) + " ";
            ret += "\n";
        }

        return ret;
    }

    // Tile at (row, col) or 0 if blank
    public int tileAt(int row, int col) {

        // Dealing with parameters out of bounderyeshttps://www.geeksforgeeks.org/java-program-for-count-inversions-in-an-array-set-1-using-merge-sort/
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();

        return tiles[row][col];

    }

    // Board size
    public int size() {
        return n;
    }

    // Number of tiles out of place
    public int hamming() {

        return hammingN;
    }

    // Sum of Manhattan distances between tiles and goal
    public int manhattan() {

        return manhattanN;
    }

    // Is the board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // Does this board equal y?
    public boolean equals(Object y) {

        // Checking if objects are the same
        if (y == tiles)
            return true;

        // Checking if argument is null
        if (y == null)
            return false;

        // Checking if they are of the same type
        if (y.getClass() != tiles.getClass())
            return false;

        // Casting matrix 'cmp' from object 'y'
        int[][] cmp = (int[][]) y;

        // Comparing if they have same size
        if (tiles[0].length != cmp[0].length || tiles.length != cmp.length)
                return false;

        // Comparing each element
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] != cmp[i][j])
                    return false;

        // Everything was fine, so they are equal
        return true;
    }


    public Iterable<Board> neighboors() {

        // New returnable object
        Queue<Board> ret = new Queue<>();

        int[] zeroPos = {-1, -1};// Position {row, col} of blankTile
        int[][] dir = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};  // Available directions

        // Finding position of blank tile
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n && zeroPos[0] == -1; j++) {
                if (tiles[i][j] == 0) {
                    zeroPos[0] = i;
                    zeroPos[1] = j;
                }
            }
        }

        // Going through all possible neighboors
        for (int i = 0; i < 4; i++){

            // Copying original matrix
            int[][] aux = new int[n][n];
            for (int j = 0; j < n; j++)
                aux[j] = tiles[j].clone();

            // Getting neighboors postions
            int rowPos = zeroPos[0] + dir[i][0];
            int colPos = zeroPos[1] + dir[i][1];

            // Checking if they are valid
            if (rowPos < 0 || rowPos >= n || colPos < 0 || colPos >= n)
                continue;

            // Exchanging their values
            aux[ zeroPos[0] ][ zeroPos[1] ] = aux[rowPos][colPos];
            aux[rowPos][colPos] = 0;

            // Enqueing result as new Board
            ret.enqueue(new Board(aux));
        }

        // Returning iterable object
        return ret;
    }

    // Is this board solvable?
    public boolean isSolvable() {

        ic invcnt = new ic();
        int cnt = invcnt.count();

        // If size is odd
        if (n%2 == 1)
            return (cnt%2 == 0);

        // If size is even
        return ((cnt + blankRow)%2 == 1);
    }

    // Implementation of an algorithm to count inversions in O(nlogn),
    // using MergeSort (divide and conquer) technic
    private class ic {

        // Arrays to help
        int aux[] = new int[n*n];
        int arr[] = new int[n*n];

        // Constructor method
        public ic () {

            // Assigning values to 1D array
            int counter = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {

                    // Ignoring blank tile
                    if (tiles[i][j] == 0) continue;

                    arr[counter] = tiles[i][j];
                    aux[counter] = tiles[i][j];
                    counter++;
                }
            }
        }

        // Return inversion count
        public int count(){

            return _mergeSort(arr, aux, 0, n*n-1);

        }

        // Applying divide and conquer technique
        private int _mergeSort(int arr[], int aux[], int left, int right) {

            int mid, inv_count = 0;
            if (right > left) {

                // Mid of interval
                mid = (right + left) / 2;

                // Recursive call
                inv_count = _mergeSort(arr, aux, left, mid);
                inv_count += _mergeSort(arr, aux, mid + 1, right);

                // Merging both parts of intrerval
                inv_count += merge(arr, aux, left, mid + 1, right);
            }

            // Inversion value
            return inv_count;
        }

        // Merge two blocks in order
        private int merge(int arr[], int aux[], int left, int mid, int right) {

            int i, j, k;
            int inv_count = 0;

            i = left;
            j = mid;
            k = left;

            // Sorting and counting inversions
            while ((i <= mid - 1) && (j <= right)) {
                if (arr[i] <= arr[j])
                    aux[k++] = arr[i++];

                else {
                    aux[k++] = arr[j++];
                    inv_count = inv_count + (mid - i);
                }
            }

            // Completing arrays
            while (i <= mid - 1)
                aux[k++] = arr[i++];

            while (j <= right)
                aux[k++] = arr[j++];

            for (i = left; i <= right; i++)
                arr[i] = aux[i];

            // Returng inversion value
            return inv_count;
        }
    }

    // Unit test
    public static void main(String[] args) {

        // javac-algs4 Board.java && java-algs4 Board

        int[][] aux = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        Board b = new Board(aux);

        StdOut.println(b.toString());
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());

        int[][] teste = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

        StdOut.println(b.equals(teste));
        StdOut.println(b.equals(aux));

        for (Board u : b.neighboors()){
            StdOut.println(u.toString());
        }

        StdOut.println(b.isSolvable());
    }
}

// COMENTAR UNIT TEST E PRINTAR OQ TA ACONTESSENO
