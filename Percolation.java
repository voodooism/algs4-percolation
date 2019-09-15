/* *****************************************************************************
 *  Name:    Just Me
 *  NetID:   voodooism
 *  Precept: P00
 *
 *  Description:  This class implements percolation system
 *  using an n-by-n grid of sites. Each site is either open or blocked.
 *  A full site is an open site that can be connected to an open site in
 *  the top row via a chain of neighboring (left, right, up, down) open sites.
 *  We say the system percolates if there is a full site in the bottom row.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final byte OPEN = 0b001;
    private static final byte CONNECTED_TOP = 0b010;
    private static final byte CONNECTED_BOTTOM = 0b100;

    // This flag indicates whether system percolates or not
    private boolean canPercolates = false;

    // Union-find structure for the storing connected sites
    private final WeightedQuickUnionUF unionUF;

    // Stores state of each site
    private byte[] statuses;

    // Stores count of open sites
    private int countOfOpenSites;

    // Stores grid's side length
    private final int sideLength;

    // Creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        sideLength = n;

        int numberOfSites = n * n;
        unionUF = new WeightedQuickUnionUF(numberOfSites);
        statuses = new byte[numberOfSites];
        countOfOpenSites = 0;

        for (int i = 0; i < n; i++) {
            addState(i, CONNECTED_TOP);
            addState(numberOfSites - 1 - i, CONNECTED_BOTTOM);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isCorrectIndexes(row, col)) {
            throw new IllegalArgumentException();
        }

        if (!isOpen(row, col)) {
            addState(calculateSiteIndex(row, col), OPEN);
            countOfOpenSites++;
            connectWithAdjacent(row, col);
        }
    }

    // Is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isCorrectIndexes(row, col)) {
            throw new IllegalArgumentException();
        }

        return hasState(calculateSiteIndex(row, col), OPEN);
    }

    // Is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isCorrectIndexes(row, col)) {
            throw new IllegalArgumentException();
        }

        int rootIdex = unionUF.find(calculateSiteIndex(row, col));
        return hasState(rootIdex, OPEN) && hasState(rootIdex, CONNECTED_TOP);
    }

    // Does the system percolate?
    public boolean percolates() {
        return canPercolates;
    }

    // Returns the number of open sites
    public int numberOfOpenSites() {
        return countOfOpenSites;
    }

    // Connects adjacent sites
    private void connectWithAdjacent(int row, int col) {
        int[][] adjacent = {
                { row - 1, col },
                { row + 1, col },
                { row, col - 1 },
                { row, col + 1 }
        };
        int currentSiteIndex = calculateSiteIndex(row, col);

        byte newState = statuses[currentSiteIndex];
        for (int i = 0; i < adjacent.length; i++) {
            int adjacentRow = adjacent[i][0];
            int adjacentCol = adjacent[i][1];
            if (isCorrectIndexes(adjacentRow, adjacentCol)) {
                int adjacentRootIndex = unionUF.find(calculateSiteIndex(adjacentRow, adjacentCol));
                if (hasState(adjacentRootIndex, OPEN)) {
                    newState |= statuses[adjacentRootIndex];
                    unionUF.union(currentSiteIndex, adjacentRootIndex);
                }
            }
        }
        int newRoot = unionUF.find(currentSiteIndex);
        addState(newRoot, newState);
        if (isSiteConnectedBoth(newRoot)) {
            canPercolates = true;
        }
    }

    // Converts two-dimensional indexes to one-dimensional index
    private int calculateSiteIndex(int row, int col) {
        return sideLength * (row - 1) + col - 1;
    }

    // Checks whether indexes are correct
    private boolean isCorrectIndexes(int row, int col) {
        return row > 0 && col > 0 && row <= sideLength && col <= sideLength;
    }

    // Is the site's state contains checked state?
    private boolean hasState(int siteIndex, byte state) {
        return (statuses[siteIndex] & state) == state;
    }

    // Updates state of site by given site index
    private void addState(int siteIndex, byte newState) {
        statuses[siteIndex] |= newState;
    }

    // Is the site connected with any top and any bottom site at the same time?
    private boolean isSiteConnectedBoth(int siteIndex) {
        return hasState(siteIndex, CONNECTED_BOTTOM)
                && hasState(siteIndex, CONNECTED_TOP);
    }

    // Test client (optional)
    public static void main(String[] args) {
        // test client can be here
    }
}
