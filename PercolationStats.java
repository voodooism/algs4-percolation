/* *****************************************************************************
 *  Name:    Just Me
 *  NetID:   voodooism
 *  Precept: P00
 *
 *  Description:  This class do Monte Carlo simulation.
 *  It creates n-by-n grid of sites and do M trials.
 *  Each trial is done as follows:
 *      * Choose a site uniformly at random among all blocked sites.
 *      * Open the site.
 *      * Repeat until the system percolates.
 *  The fraction of sites that are opened when the system percolates
 *  provides an estimate of the percolation threshold.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    // Contains a count of open sites at which system is percolated for each trial
    private final double[] countOfOpenSites;

    // Stores the sample mean of percolation threshold
    private final double mean;

    // Stores sample standard deviation of percolation threshold
    private final double stddev;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        countOfOpenSites = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(
                        StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            countOfOpenSites[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(countOfOpenSites);
        stddev = StdStats.stddev(countOfOpenSites);
    }

    // Sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // Sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // Low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - ((CONFIDENCE_95 * stddev()) / Math.sqrt(countOfOpenSites.length));
    }

    // High endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + ((CONFIDENCE_95 * stddev()) / Math.sqrt(countOfOpenSites.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridSide = Integer.parseInt(args[0]);
        int trialsNumber = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(gridSide, trialsNumber);
        StdOut.println("mean = " + stats.mean);
        StdOut.println("stddev = " + stats.stddev());
        StdOut.println("95% confidence interval = " + "[" + stats.confidenceLo()
                               + ", " + stats.confidenceHi() + "]");
    }
}
