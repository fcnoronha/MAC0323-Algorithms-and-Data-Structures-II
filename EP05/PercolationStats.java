
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

                // Incraseing counter and opening it
                cnt += 1.0;
                pc.open(row, col);
            }

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
        StdOut.println("mean() = " + pcs.mean());
        StdOut.println("stddev() = " + pcs.stddev());
        StdOut.println("confidenceLow() = " + pcs.confidenceLow());
        StdOut.println("confidenceHigh() = " + pcs.confidenceHigh());
        StdOut.println("elapsed time = " + stw.elapsedTime());
    }
}
