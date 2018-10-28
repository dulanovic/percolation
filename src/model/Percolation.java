package model;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF wquf;
    private int noOpen;
    private final int dimension;
    private boolean[] open;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal value ---> " + n + ", value for dimension of the grid must be >0.");
        }
        wquf = new WeightedQuickUnionUF(n * n + 2);
        noOpen = 0;
        dimension = n;
        open = new boolean[n * n];
        initializeVirtualSites();
        // draw();
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkInputValidity(row, col);
        // System.out.println("row ---> " + row + ", col ---> " + col);
        int index = (row - 1) * dimension + col - 1;
        if (!open[index]) {
            open[index] = true;
            noOpen++;
            connectWithNeighboringOpenSites(index);
            // draw();
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkInputValidity(row, col);
        return open[dimensionTransform(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkInputValidity(row, col);
        if (dimension == 1) {
            return open[0] == true;
        } else {
            return (isOpen(row, col) && wquf.connected(dimension * dimension, dimensionTransform(row, col)));
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return noOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        if (dimension == 1) {
            return open[0] == true;
        } else {
            return wquf.connected(dimension * dimension, dimension * dimension + 1);
        }
    }

    // checks whether values for row and column are valid
    private void checkInputValidity(int row, int col) {
        if (row <= 0 || row > dimension) {
            throw new IllegalArgumentException("Row index ---> " + row + " out of bounds, maximum value ---> " + dimension);
        }
        if (col <= 0 || col > dimension) {
            throw new IllegalArgumentException("Column index ---> " + col + " out of bounds, maximum value ---> " + dimension);
        }
    }

    // connecting virtual sites to sites from top and bottom rows
    private void initializeVirtualSites() {
        if (dimension == 1) {
            return;
        }
        for (int i = 0; i < dimension; i++) {
            wquf.union(i, dimension * dimension);
        }
        for (int i = dimension * dimension - 1; i >= dimension * (dimension - 1); i--) {
            wquf.union(i, dimension * dimension + 1);
        }
    }

    // transforms indices from 2D grid to 1D array
    private int dimensionTransform(int row, int col) {
        return (row - 1) * dimension + col - 1;
    }

    // check if neighboring sites are open and connect to them if they are
    private void connectWithNeighboringOpenSites(int index) {
        int neighbourUp = index - dimension;
        int neighbourRight = index + 1;
        int neighbourDown = index + dimension;
        int neighbourLeft = index - 1;
        if (neighbourUp >= 0 && neighbourUp < dimension * dimension) {
            if (open[neighbourUp]) {
                wquf.union(index, neighbourUp);
                // System.out.println("connected ---> [" + index + ", " + neighbourUp + "]");
            }
        }
        if (neighbourRight >= 0 && neighbourRight < dimension * dimension && sameRow(index, neighbourRight)) {
            if (open[neighbourRight]) {
                wquf.union(index, neighbourRight);
                // System.out.println("connected ---> [" + index + ", " + neighbourRight + "]");
            }
        }
        if (neighbourDown >= 0 && neighbourDown < dimension * dimension) {
            if (open[neighbourDown]) {
                wquf.union(index, neighbourDown);
                // System.out.println("connected ---> [" + index + ", " + neighbourDown + "]");
            }
        }
        if (neighbourLeft >= 0 && neighbourLeft < dimension * dimension && sameRow(index, neighbourLeft)) {
            if (open[neighbourLeft]) {
                wquf.union(index, neighbourLeft);
                // System.out.println("connected ---> [" + index + ", " + neighbourLeft + "]");
            }
        }
    }

    // are indices in the same row?
    private boolean sameRow(int index1, int index2) {
        int row1 = ((int) Math.ceil(index1 / dimension)) + 1;
        int row2 = ((int) Math.ceil(index2 / dimension)) + 1;
        return row1 == row2;
    }

    /*private void draw() {
        for (int i = 0; i < dimension * dimension; i++) {
            if (i % dimension == 0) {
                System.out.println("\n");
            }
            System.out.print(wquf.find(i));
            if (open[i]) {
                System.out.print("OOO");
                if (isFull((i / dimension) + 1, (i % dimension) + 1)) {
                    System.out.print("-F");
                }
            } else {
                System.out.print("XXX");
                if (isFull((i / dimension) + 1, (i % dimension) + 1)) {
                    System.out.print("-F");
                }
            }
            System.out.print("\t");
        }
        System.out.println("[" + wquf.find(dimension * dimension) + " - " + wquf.find(dimension * dimension + 1) + "]");
    }*/
    public static void main(String[] args) {
        // for testing purposes only
    }

}
