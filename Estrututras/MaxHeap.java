import edu.princeton.cs.algs4.*;

public class MaxHeap {
	// Sort strings using heapsort. Will sort the first string passed as
	// paramether (args[0]) in the call of the application.

	public static void sort(String[] pq) {
		// Will transform the givem string in a max-heap

        int n = pq.length;
        for (int k = n/2; k >= 1; k--)
            sink(pq, k, n);
        while (n > 1) {
            swap(pq, 1, n--);
            sink(pq, 1, n);
        }
    }

    private static void sink(String[] pq, int k, int n) {
		// "Descend" character to right position

        while (2*k <= n) {
            int j = 2*k;
            if (j < n && less(pq, j, j+1)) j++;
            if (!less(pq, k, j)) break;
            swap(pq, k, j);
            k = j;
        }
    }

    private static boolean less(String[] pq, int i, int j) {
        return pq[i-1].compareTo(pq[j-1]) < 0;
    }

    private static void swap(Object[] pq, int i, int j) {
		// Swap two terms

        Object aux = pq[i-1];
        pq[i-1] = pq[j-1];
        pq[j-1] = aux;
    }

    private static void print(String[] a) {
		// Print actual String

        for (int i = 0; i < a.length; i++)
            StdOut.printf("%s", a[i]);
        StdOut.println();
    }

    public static void main(String[] args) {
		char[] c = args[0].toCharArray();
        String[] a = new String[args[0].length()];
		for (int i = 0; i < args[0].length(); i++)
			a[i] = String.valueOf(c[i]);
        MaxHeap.sort(a);
        print(a);
    }
}
