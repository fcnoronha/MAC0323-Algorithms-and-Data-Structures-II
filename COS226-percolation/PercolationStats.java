import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    double[] ths; // Will store calculated thresholds
    private int auxt; // Stores t value

    // Perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        ths = new double[trials];
        auxt = trials;

        // Performing each trial
        for (int t = 0; t < trials; t++) {

            // Creating new percolations
            Percolation pc = new Percolation(n);

            // Number of cells until percolates
            double cnt = 0.0;
            while (!pc.percolates()) {

                // Choose a site uniformly at random among all blocked sites
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);

                // If it's already open
                if (pc.isOpen(row, col))
                    continue;

                // Opening it
                pc.open(row, col);
            }

            // Getting number of open sites
            cnt = (double)pc.numberOfOpenSites();

            // Storing
            ths[t] = cnt/(n*n);
        }
    }

    // Sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(ths);
    }

    // Sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(ths);
    }

    // Sow endpoint of 95% confidence interval
    public double confidenceLow() {
        return this.mean() - ((1.96 * this.stddev())/Math.sqrt(auxt));
    }

    // High endpoint of 95% confidence interval
    public double confidenceHigh() {
        return this.mean() + ((1.96 * this.stddev())/Math.sqrt(auxt));
    }

    // test client (see below)
    public static void main(String[] args) {

        // javac-algs4 PercolationStats.java && java-algs4 PercolationStats

        // Watch to the time spent in the application
        Stopwatch stw = new Stopwatch();

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        if (n <= 0 || t <= 0)
            throw new IllegalArgumentException();

        PercolationStats pcs = new PercolationStats(n, t);

        // Printing stuff
        StdOut.printf("mean() \t\t\t = %.6f\n", pcs.mean());
        StdOut.printf("stddev() \t\t = %.6f\n",pcs.stddev());
        StdOut.printf("confidenceLow() \t = %.6f\n", pcs.confidenceLow());
        StdOut.printf("confidenceHigh() \t = %.6f\n", pcs.confidenceHigh());
        StdOut.printf("elapsed time \t\t = %.3f\n", stw.elapsedTime());
    }
}
