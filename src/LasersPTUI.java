import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;    //SCANNER

/**
 * @author Moses Lagoon
 * @author Jordan Edward Shea
 * @author Jennifer Liu
 *
 * Job Specifications
 * Moses: Constructor, Display, Help
 * Jordan: Remove, Quit
 * Jennifer: Add, Errors, Verify
 *
 * Added Methods
 */

public class LasersPTUI {
    private char[][] grid;    // safe

    //MOSES LAGOON - Constructor for LasersPTUI

    public int rDIM;    //row dimension of the safe
    public int cDIM;    //column dimension of the safe


    /**
     * The constructor reads the the file and constructs an initial safe(grid).
     *
     *
     * @param filename - the name of the file
     * @throws FileNotFoundException
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename)); //scanning the file in
        this.rDIM = in.nextInt();    //Reading in the dimension of the safe
        this.cDIM = in.nextInt();
        this.grid = new char[rDIM][cDIM];// creating initial safe grid with their
                                        // respective row and col dimensions

        //CONSTRUCTING THE GRID BY ADDING VALUES
        for (int r =0; r <rDIM; r++){
            for(int c = 0; c < cDIM; c++){
                String s = in.next();
                this.grid[r][c] = s.charAt(0);
            }
        }

    }

    public void display(){
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
        System.out.println(result);


        //JUST CHECKING WITH SOLUTION:
        /**
        System.out.println("  0 1 2 3\n" +
                "  -------\n" +
                "0|* L * 0\n" +
                "1|* X L *\n" +
                "2|L * 1 *\n" +
                "3|1 * * L");
       */
    }

    /**
     * The help command displays the help message to standard output, with no
     * status message. Continuing with the above sample run:
     */

    public void helpMessage(){
        System.out.print(
                "> help\n"+
                "a|add r c: Add laser to (r,c)\n" +
                "d|display: Display safe\n" +
                "h|help: Print this help message\n" +
                "q|quit: Exit program\n" +
                "r|remove r c: Remove laser from (r,c)\n" +
                "v|verify: Verify safe correctness");
        // Jordan Shea
        System.out.println();
        boolean validInput = true;
        while (validInput){
            Scanner scnInput = new Scanner(System.in);
            switch(scnInput.next()){
                case "a":
                    int r = scnInput.nextInt();
                    int c = scnInput.nextInt();
                    add(r,c);
                    break;
                case "d":
                    display();
                    break;
                case "h":
                    helpMessage();
                    break;
                case "q":
                    quit();
                    break;
                case "r":
                    int row = scnInput.nextInt();
                    int column = scnInput.nextInt();
                    remove(row, column);
                    break;
                case "v":
                    System.out.println(verify());
                    break;
                default:
                    System.out.print("Error: Invalid Input");
                    validInput = false;
            }
        }
    }

    //MOSES LAGOON

    //JENNIFER LIU
    /**
     * Verify command displays a status message that indicates whether the safe is
     * valid or not. In order to be valid, none of the rules of the safe may be
     * violated. Each tile that is not a pillar must have either a laser or beam
     * covering it. Each pillar that requires a certain number of neighboring lasers
     * must add up exactly. If two or more lasers in the sight of each other,
     * in the cardinal direction, it is invalid
     * @return String - status message
     *
     */
    public String verify(){

        boolean isValid = true;
        for(int row = 0; row < rDIM; row++){
            for(int col = 0; col < cDIM; col++){
                //If the current object at this position is a Laser,
                //call verifyWithPos
                //If one of the tiles are empty, return an error
                if(grid[row][col] == '.'){
                    return "Error verifying at: (" + row + ", " + col + ")";
                }
                //If there are more than one laser in the same row or column,
                //return false
                if(grid[row][col] == 'L'){
                    if(!verifyWithPos(row,col)){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }
                }
                //If there is a power outlet, make sure that the number of lasers
                //surrounding the outlet matches with its number
                //In here, we call the checkNeighbors method to help us do this
                else if(grid[row][col] == '0'){
                    //put conditions
                    if(!checkNeighbors(0, row, col)){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }

                }
                else if(grid[row][col] == '1'){
                    //put condition
                    if(!checkNeighbors(1, row, col)){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }

                }
                else if(grid[row][col] == '2'){
                    //put condition
                    if(!checkNeighbors(2, row, col)){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }
                }
                else if(grid[row][col] == '3'){
                    //put condition
                    if(!checkNeighbors(3, row, col)){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }
                }
                else if(grid[row][col] == '4'){
                    //put condition
                    if(!checkNeighbors(4, row, col)){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }
                }
            }
        }

        return "Safe is fully verified";
    }

    /**
     * This ensures that the number of lasers that are adjacent to
     * the power outlet matches with the number that is stated
     * in the power outlet. For instance, if the power outlet only
     * allows for 1 lasers to be plugged adjacenet to it, there
     * should only be 1 lasers in total for all of the possible
     * locations that an outlet could be placed.
     * @param num - (int) the number of lasers allowed by the outlet
     * @param row - (int) current row
     * @param col - (int) current col
     * @return boolean - returns false if the number of lasers
     * does not match with the number that a power outlet can
     * handle
     */
    public boolean checkNeighbors(int num, int row, int col){
        int numLasers = 0;
        if(row == 0 && col == 0){
            if(grid[row][col+1] == 'L'){
                numLasers++;
            }
            if(grid[row+1][col] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checks for the top right corner (0, cDIM)
        else if(row == 0 && col == cDIM-1){
            if(grid[row][col-1] == 'L'){
                numLasers++;
            }
            if(grid[row+1][col] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checks for the bottom left corner (0, rDIM)
        else if(row == rDIM -1 && col == 0){
            if(grid[row-1][col] == 'L'){
                numLasers++;
            }
            if(grid[row][col+1] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checks for the bottom right corner (rDIM, cDIM)
        else if(row == rDIM-1 && col == cDIM-1){
            if(grid[row][col-1] == 'L'){
                numLasers++;
            }
            if(grid[row-1][col] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checking for the sides
        //The left side
        else if(col == 0 && row > 0){
            if(grid[row-1][col] == 'L'){
                numLasers++;
            }
            if(grid[row+1][col] == 'L'){
                numLasers++;
            }
            if(grid[row][col+1] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for Top Side
        else if(col > 0 && row == 0){
            if(grid[row+1][col] == 'L'){
                numLasers++;
            }
            if(grid[row][col-1] == 'L'){
                numLasers++;
            }
            if(grid[row][col+1] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for the Right Side
        else if(col == cDIM-1 && row > 0){
            if(grid[row+1][col] == 'L'){
                numLasers++;
            }
            if(grid[row][col-1] == 'L'){
                numLasers++;
            }
            if(grid[row-1][col] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for the bottom Side
        else if(row == rDIM-1 && col > 0){
            if(grid[row-1][col] == 'L'){
                numLasers++;
            }
            if(grid[row][col-1] == 'L'){
                numLasers++;
            }
            if(grid[row][col+1] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for the cells that are inside the
        //grid
        else{
            if(grid[row-1][col] == 'L'){
                numLasers++;
            }
            if(grid[row][col-1] == 'L'){
                numLasers++;
            }
            if(grid[row][col+1] == 'L'){
                numLasers++;
            }
            if(grid[row+1][col] == 'L'){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        return true;
    }

    /**
     * This ensures that two or more lasers are not in sight
     * of each other. If they are return false
     * @param r - (int) current row
     * @param c - (int) current col
     * @return
     */
    public boolean verifyWithPos(int r, int c){

        //Check to make sure that the there is
        //only 1 laser horizontally placed
        for(int col = 0; col < cDIM; col++){
            if(grid[r][col] == 'L' && col != c){
                return false;
            }
        }

        //Check to make sure that there is only 1
        //laser vertically placed
        for(int row = 0; row < rDIM; row++){
            if(grid[row][c] == 'L' && row != r){
                return false;
            }
        }
        return true;
    }
    //JENNIFER LIU

    public char[][] remove(int r, int c){
        return null;
    }

    public String add(int r, int c){
        return "";
    }

    public void quit(){
        System.exit(2);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 1) {
            //MOSES LAGOON
            LasersPTUI lasers = new LasersPTUI(args[0]);
            lasers.display();           //PRINTING DISPLAY HERE
            lasers.helpMessage();
            //MOSES LAGOON
        } else if (args.length == 2) {
            //MOSES LAGOON
            //Creating a new lasers object to print out display
            LasersPTUI lasers = new LasersPTUI(args[0]);
            lasers.display();
            //MOSES LAGOON
        } else {
            System.out.println("Usage: java LasersPTUI safe-file [input]");
        }
    }
}
