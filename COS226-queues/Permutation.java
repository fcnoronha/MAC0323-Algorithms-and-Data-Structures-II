import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {

        // javac-algs4 Permutation.java && java-algs4 Permutation 3 < t.txt

        int n = Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        String s;
        // Reads while there is input
        while (!StdIn.isEmpty()) {

            s = StdIn.readString();
            rq.enqueue(s);
        }

        // Iterating through
        Iterator<String> it = rq.iterator();
        for (int i = 0; i < n; i++) {
            StdOut.println(it.next());
        }
    }
}
