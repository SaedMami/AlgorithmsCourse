/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java InteractivePercolationVisualizer N T
 *  Dependencies: Percolation.java
 *                StdRandom.java StdIn.java StdOut.java StdStats
 *
 *  This program takes the grid size N as a command-line argument.
 *  and run T independent experiments to estimate the percolation threshold
 *  by taking the mean of the T experiments, as well as calulation the std deviation
 *  and 95% confidence interval
 ******************************************************************************/
/**
 * @author Saed
 *
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    // private Percolation _grid; // N-N percolation grid to perform the tests
    // on
    private double[] _thresholds; // holding the approximated threshold for T
                                  // independent experiments
    private double _mean; // the average value of the percolation threshold
    private double _stddev; // standard deviation of the percolation threshold
    private double _confidenceLo; // low endpoint of 95% confidence interval
    private double _confidenceHi; // high endpoint of 95% confidence interval

    /**
     * Initializes a PercolationStats object and performs T independent
     * experiment to estimate the percolation threshold, and computes some
     * statistical quantities from the experiments namely, the mean and standard
     * deviation of the percolation threshold, as well as, the low and high end
     * points of the 95% confidence interval
     *
     * @param N
     *            the dimension of the percolation grid
     * 
     * @param T
     *            the number of independent experiment performed to estimate the
     *            percolation threshold
     * @throws IllegalArgumentException
     *             if N <= 0 or T <= 0
     */

    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("invalid arguments");
        }

        _thresholds = new double[T];

        // perform T independent experiments
        for (int k = 0; k < T; ++k) {
            Percolation _grid = new Percolation(N); // initialize a an N-N grid
                                                    // of all
            // closed sites
            int openSites = 0;

            // keep opening random grid sites until the system percolates
            while (!_grid.percolates()) {
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);

                if (!_grid.isOpen(i, j)) {
                    _grid.open(i, j);
                    openSites++;
                }
            }

            // the system has percolated, record the percolation threshold
            _thresholds[k] = (double) openSites / (N * N);
        }

        // Now we are ready to calculate some statistics about the experiments
        _mean = StdStats.mean(_thresholds);
        _stddev = StdStats.stddev(_thresholds);
        _confidenceLo = _mean - (1.96 * _stddev / Math.sqrt(T));
        _confidenceHi = _mean + (1.96 * _stddev / Math.sqrt(T));
    }

    /**
     * @return the sample mean of the percolation threshold over all the
     *         performed experiments
     */
    public double mean() {
        return _mean;
    }

    /**
     * @return the sample standard deviation of the percolation threshold over
     *         all the performed experiments
     */
    public double stddev() {
        return _stddev;
    }

    /**
     * @return the low end point of the 95% confidence interval
     */
    public double confidenceLo() {
        return _confidenceLo;
    }

    /**
     * @return the high end point of the 95% confidence interval
     */
    public double confidenceHi() {
        return _confidenceHi;
    }

    /**
     * Takes the size of a percolation grid N, and number of experiments T to
     * estimate the percolation threshold of an N-N grid using Monte Carlo
     * Simulation, and prints out some statistical quantities from the
     * experiments namely, the mean and standard deviation of the percolation
     * threshold, as well as the low and high end points of the 95% confidence
     * interval and prints them out on standard output
     *
     * @param N
     *            the dimension of the percolation grid
     * 
     * @param T
     *            the number of independent experiment performed to estimate the
     *            percolation threshold
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            int N = Integer.parseInt(args[0]);
            int T = Integer.parseInt(args[1]);

            Stopwatch watch = new Stopwatch();
            PercolationStats experiment = new PercolationStats(N, T);
            double time = watch.elapsedTime();
            System.out.format("mean = %f \n", experiment.mean());
            System.out.format("stddev = %f \n", experiment.stddev());
            System.out.format("95%% confidence interval = %f , %f \n", experiment.confidenceLo(),
                    experiment.confidenceHi());
            System.out.format("elapsed time = %f \n", time);
        }
    }

}
