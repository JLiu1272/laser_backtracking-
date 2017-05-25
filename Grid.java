/**
 * The Grid class represents the potential solution to the puzzle that is read
 * in from the input file. Internally, this class uses a native 2-D array. the
 * grid is row major (followed by column minor).
 *
 * @author Jennifer Liu
 * @author Jordan Shea
 * @author Moses Lagoon
 * */

public class Grid {

    private int[][] grid;     //the 2-D array of integers that is instantiated in
    // the constructor
    /**
     * Constructs the grid.
     * @param dim - the square dimensionality of the grid
     * */
    public Grid(int dim) {
        this.grid = new int[dim][dim];
    }

    /**
     * The setVal method. Stores a value in the grid at (row, col).
     *
     * @param row - the row (0..dim)
     * @param col - the column (0..dim)
     * @param val - the value to store
     * */

    public void setVal(int row, int col, int val) {
        this.grid[row][col] = val;
    }

    /**
     * The gevVal method retrieves the value in the grid at (row, col).
     *
     * @param row - the row (0..dim)
     * @param col - the column (0..dim)
     * @return - the value t (row, col)
     * */
    public int getVal(int row, int col) {
        return this.grid[row][col];
    }
}

