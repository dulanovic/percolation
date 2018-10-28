package stats;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import model.Percolation;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final double[] results;
    private final int trials;
    private final int dimension;

    private double mean;
    private double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid input value for dimension of the grid. Input must be >0.");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("Invalid input value for number of experiments. Input must be >0.");
        }
        results = new double[trials];
        this.trials = trials;
        dimension = n;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            int row, col;
            while (!p.percolates()) {
                do {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                } while (p.isOpen(row, col));
                p.open(row, col);
            }
            double result = (double) p.numberOfOpenSites() / (n * n);
            results[i] = result;
        }
        // writeReport();
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(results);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trials == 1) {
            return Double.NaN;
        }
        stddev = StdStats.stddev(results);
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONFIDENCE_95 * stddev) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONFIDENCE_95 * stddev) / Math.sqrt(trials);
    }

    // writes report from a series of experiments to standard output
    private void writeReport() {
        StdOut.printf("Arguments, grid size = %d, number of experiments = %d\n<<<------------------------>>>\n", dimension, trials);
        StdOut.printf("Mean = %f\nStandard deviation = %f\n95%% confidence interval = [%f, %f]\n", mean(), stddev(), confidenceLo(), confidenceHi());
    }

    // runs simulation
    private static void run() {
        int n = Integer.parseInt(StdIn.readLine());
        int experiments = Integer.parseInt(StdIn.readLine());
        PercolationStats ps = new PercolationStats(n, experiments);
        ps.writeReport();
    }

    // for testing purposes only
    public static void main(String[] args) {
        run();
    }

}
