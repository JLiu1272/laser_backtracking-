package backtracking;

import model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author Jennifer Liu
 */

public class SafeConfig implements Configuration{

    private int rDIM;
    private int cDIM;
    private char[][] grid;
    //Tells me where my current position is
    int currRow;
    int currCol;

    private LasersModel lasersModel;

    public SafeConfig(String filename) throws FileNotFoundException {
        // TODO
        Scanner in = new Scanner(new File(filename)); //scanning the file in
        this.rDIM = in.nextInt();    //Reading in the dimension of the safe
        this.cDIM = in.nextInt();
        this.grid = new char[rDIM][cDIM];// creating initial safe grid with their
        // respective row and col dimensions

        //Current Position
        currRow = 0;
        currCol = 0;

        lasersModel = new LasersModel(filename);

        //CONSTRUCTING THE GRID BY ADDING VALUES
        for (int r =0; r <rDIM; r++){
            for(int c = 0; c < cDIM; c++){
                String s = in.next();
                this.grid[r][c] = s.charAt(0);
            }
        }

    }

    /**
     * A copy constructor, it is needed in the getSuccessor method
     * @param other - (SafeConfig) The safeconfig that we would like
     *                 to copy
     */
    public SafeConfig(SafeConfig other){
        this.rDIM = other.rDIM;
        this.cDIM = other.cDIM;
        this.currRow = other.currRow;
        this.currCol = other.currCol;
        this.grid = new char[rDIM][cDIM];
        this.lasersModel = other.lasersModel;

        for(int row=0; row < other.rDIM;row++){
            System.arraycopy(other.grid[row],0,this.grid[row],0,other.cDIM);
        }

    }

    @Override
    public Collection<Configuration> getSuccessors(){

        ArrayList<Configuration> successors = new ArrayList<>();

        //If it hits an empty spot, generate a successor with a laser that
        //that spot, also generate a an empty successor at the slot
        if(grid[currRow][currCol] == '.'){
            SafeConfig lasersAdded = new SafeConfig(this);
            lasersModel.addWithGrid(currRow,currCol,lasersAdded.grid);
            SafeConfig empty = new SafeConfig(this);
            successors.add(lasersAdded);
            successors.add(empty);
        }
        else{
            //If it is a pillar, generate a successor with no changes made to it
            SafeConfig noChange = new SafeConfig(this);
            successors.add(noChange);
        }

        return successors;
    }

    @Override
    public boolean isValid() {
        // TODO
        //Check to make sure that no means are in the way of the current one
        //in the horizontal and vertical direction
        if(grid[currRow][currCol] == 'L') {
            //East Direction
            for (int row = currRow + 1; row < rDIM; row++) {
                if (grid[row][currCol] == 'L') {
                    return false;
                } else if (grid[row][currCol] != '.' && grid[row][currCol] != '*') {
                    break;
                }
            }
            //West Direction
            for (int row = currRow - 1; row >= 0; row--) {
                if (grid[row][currCol] == 'L') {
                    return false;
                } else if (grid[row][currCol] != '.' && grid[row][currCol] != '*') {
                    break;
                }
            }
            //South Direction
            for (int col = currCol + 1; col < cDIM; col++) {
                if (grid[currRow][col] == 'L') {
                    return false;
                } else if (grid[currRow][col] != '.' && grid[currRow][col] != '*') {
                    break;
                }
            }
            //North Direction
            for (int col = currCol - 1; col >= 0; col--) {
                if (grid[currRow][col] == 'L') {
                    return false;
                } else if (grid[currRow][col] != '.' && grid[currRow][col] != '*') {
                    break;
                }
            }
        }

        //Check to make sure that the the number of
        //laser surrounding the pillars are less than or equal to
        //the number of lasers that the pillar can take
        if(!lasersModel.verifyGridCheck(grid)){
            return false;
        }

        //If there is an empty slot in the grid, then
        //it means it is an invalid grid
        if(currRow == rDIM-1 && currCol == cDIM-1){
            for(int row = 0; row < rDIM;row++){
                for(int col = 0; col < cDIM;col++){
                    if(grid[row][col] == '.'){
                        return false;
                    }
                }
            }
        }

        //Increment the position of the grid
        //If it has hit the end of the column,
        //Increment the row, and set column back
        //to 0
        if(currCol >= cDIM-1){
            currRow++;
            currCol = 0;
        }
        else{
            currCol++;
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        // TODO
        //If no grid are empty and
        //all pillars have the correct
        //number of lasers, return true,
        //otherwise return false
        for(int row = 0; row < rDIM; row++){
            for(int col = 0; col < cDIM; col++){
                if(grid[row][col] == '.'){
                    return false;
                }
            }
        }
        return lasersModel.verifyGridCheck(this.grid);
    }

    /**
     * Testing purpose toString
     * @return
     */
    @Override
    public String toString(){
        String result = "  ";
        /*Printing Column Numbers */
        for (int c = 0; c<cDIM; c++){
            result += " " + c%10;        //Prints out Column number here
        }
        result+= "\n" + "   ";

        /** TOP ROW DASHES "-------" */
        for(int j = 1; j<cDIM; j++){
            result += "--";         //TOP ROW DASHES HERE
        }
        result += "-\n";            //ONE MORE DASH + NEWLINE

        /** DATA BODY - rows of them */
        for (int r =0; r <rDIM; r++){
            result += r%10 + "|";                  // ROW Number and Left side bars
            for(int c = 0; c < cDIM; c++){
                result += " " + this.grid[r][c];    //filling the grid in
            }
            result += "\n";
        }
        return result;
    }

    /**
     * Return the current value in that grid
     * at that current row and that current column
     * @param currRow - the row of the value
     * @param currCol - the col of the value
     * @return a char value
     */
    public char getCurrentValue(int currRow, int currCol){
        return this.grid[currRow][currCol];
    }

    public char[][] getGrid(){
        return this.grid;
    }

    /**
     * Return the current row
     * @return int - current row
     */
    public int getCurrRow(){
        return currRow;
    }

    /**
     * Return current column
     * @return int - current column
     */
    public int getCurrCol(){
        return currCol;
    }
}
