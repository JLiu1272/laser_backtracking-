/*
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;    //SCANNER

import java.util.ArrayList;

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

    /**
     * Copy Construction
     * Creating a deep copy of the safe
     * @param otherSafe (LasersPTUI)
     */
    public LasersPTUI(LasersPTUI otherSafe){
        this.cDIM = otherSafe.cDIM;
        this.rDIM = otherSafe.rDIM;

        this.grid = new char[otherSafe.rDIM][otherSafe.cDIM];
        for(int row = 0; row < otherSafe.rDIM; row++){
            System.arraycopy(otherSafe.grid[row], 0, this.grid[row], 0, otherSafe.cDIM);
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
        System.out.print(result);


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
    //MOSES LAGOON

    /**
     * The help command displays the help message to standard output, with no
     * status message. Continuing with the above sample run:
     */
    public void helpMessage() {
        System.out.print(
                    "a|add r c: Add laser to (r,c)\n" +
                    "d|display: Display safe\n" +
                    "h|help: Print this help message\n" +
                    "q|quit: Exit program\n" +
                    "r|remove r c: Remove laser from (r,c)\n" +
                    "v|verify: Verify safe correctness\n");
    }

    //JENNIFER LIU
    /**
     * Takes in user input and see which
     * command they are checking for. If the user input
     * is incorrect, it spits out the reason why it is
     * incorrect
     * @param str (String) - the user command
     */
    public void commands(String str){
        //Splits the string base on spaces
        String[] ch = str.split("\\s+");
        ArrayList<Integer> digits = new ArrayList<>();
        //Only care about the first character of the
        //first string
        char currCh = ch[0].charAt(0);
        //If the character is a digit, add it to the
        //digit arraylist
        for(int i = 1; i < ch.length; i++){
            if(ch[i].matches("\\-?\\d+")){
                digits.add(Integer.parseInt(ch[i]));
            }
        }
        //Do the actions that correspond to the command
        //Return error if the command is not valid
        switch (currCh) {
            case 'a':
                if (digits.size() < 2 || digits.size() > 2) {
                    System.out.println("Incorrect coordinates");
                } else {
                    System.out.print(add(digits.get(0), digits.get(1)) + "\n");
                }
                System.out.print("> ");
                break;
            case 'd':
                display();
                System.out.print("> ");
                break;
            case 'h':
                helpMessage();
                System.out.print("> ");
                break;
            case 'q':
                quit(); break;
            case 'r':
                if (digits.size() < 2 || digits.size() > 2) {
                    System.out.println("Incorrect coordinates");
                }
                else{
                    System.out.println(remove(digits.get(0), digits.get(1)));
                }
                System.out.print("> ");
                break;
            case 'v':
                System.out.println(verify());
                System.out.print("> ");
                break;
            default:
                System.out.println("Unrecognized command: " + str);
                System.out.print("> ");
                break;
        }
    }

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
                    if(!checkNeighbors(0, row, col, 'L').equals("true")){
                        return checkNeighbors(0,row, col,'L');
                    }

                }
                else if(grid[row][col] == '1'){
                    //put condition
                    if(!checkNeighbors(1, row, col, 'L').equals("true")){
                        return checkNeighbors(1,row, col,'L');
                    }

                }
                else if(grid[row][col] == '2'){
                    //put condition
                    if(!checkNeighbors(2, row, col, 'L').equals("true")){
                        return checkNeighbors(2,row, col,'L');
                    }
                }
                else if(grid[row][col] == '3'){
                    //put condition
                    if(!checkNeighbors(3, row, col, 'L').equals("true")){
                        return checkNeighbors(3,row, col,'L');
                    }
                }
                else if(grid[row][col] == '4'){
                    //put condition
                    if(!checkNeighbors(4, row, col, 'L').equals("true")){
                        return checkNeighbors(4,row, col,'L');
                    }
                }
            }
        }
        return "Safe is fully verified!";
    }

    /**
     * This ensures that the number of lasers that are adjacent to
     * the power outlet matches with the number that is stated
     * in the power outlet. For instance, if the power outlet only
     * allows for 1 lasers to be plugged adjacent to it, there
     * should only be 1 lasers in total for all of the possible
     * locations that an outlet could be placed.
     * @param num - (int) the number of lasers allowed by the outlet
     * @param row - (int) current row
     * @param col - (int) current col
     * @return boolean - returns false if the number of lasers
     * does not match with the number that a power outlet can
     * handle
     */
    public String checkNeighbors(int num, int row, int col, char val){
        int numLasers = 0;
        if(row == 0 && col == 0){
            if(grid[row][col+1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col + 1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row+1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col +1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
        }
        //Checks for the top right corner (0, cDIM)
        else if(row == 0 && col == cDIM-1){
            if(grid[row][col-1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col -1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row+1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row +1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
        }
        //Checks for the bottom left corner (0, rDIM)
        else if(row == rDIM -1 && col == 0){
            if(grid[row-1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r = row -1;
                    return "Error verifying at: (" + r + ", " + col + ")";
                }
            }
            if(grid[row][col+1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col +1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
        }
        //Checks for the bottom right corner (rDIM, cDIM)
        else if(row == rDIM-1 && col == cDIM-1){
            if(grid[row][col-1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col -1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row-1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row -1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
        }
        //Checking for the sides
        //The left side
        else if(col == 0 && row > 0){
            if(grid[row-1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row -1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
            if(grid[row+1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row +1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
            if(grid[row][col+1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col +1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
        }
        //Check for Top Side
        else if(col > 0 && row == 0){
            if(grid[row+1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row +1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
            if(grid[row][col-1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col -1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row][col+1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col +1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
        }
        //Check for the Right Side
        else if(col == cDIM-1 && row > 0){
            if(grid[row+1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row +1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
            if(grid[row][col-1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col -1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row-1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row -1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
        }
        //Check for the bottom Side
        else if(row == rDIM-1 && col > 0){
            if(grid[row-1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row -1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
            if(grid[row][col-1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col -1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row][col+1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col +1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
        }
        //Check for the cells that are inside the
        //grid
        else{
            if(grid[row-1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row -1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
            if(grid[row][col-1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col -1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row][col+1] == val){
                numLasers++;
                if(numLasers > num){
                    int c = col +1;
                    return "Error verifying at: (" + row + ", " + c + ")";
                }
            }
            if(grid[row+1][col] == val){
                numLasers++;
                if(numLasers > num){
                    int r= row +1;
                    return "Error verifying at: (" + row + ", " + r + ")";
                }
            }
        }
        return "true";
    }

    /**
     * This ensures that two or more lasers are not in sight
     * of each other. If they are return false
     * @param r - (int) current row
     * @param c - (int) current col
     * @return boolean value that checks whether or not lasers
     * are in sight of one another
     */
    public boolean verifyWithPos(int r, int c){
        int cRest;
        if(c != cDIM-1 && grid[r][c+1] != '*'){
            if(grid[r][c+1] == 'L'){
                return false;
            }
            cRest = c;
            //Check to make sure that the there is
            //only 1 laser horizontally placed
            for(int col = 0; col < cRest; col++){
                if(grid[r][col] == 'L'){
                    return false;
                }
            }
        }
        if(c != 0 && grid[r][c-1] != '*'){
            if(grid[r][c-1] == 'L'){
                return false;
            }
            cRest = c+1;
            //Check to make sure that the there is
            //only 1 laser horizontally placed
            for(int col = cRest; col < cDIM; col++){
                if(grid[r][col] == 'L' && col != c){
                    return false;
                }
            }
        }
        int rRest;
        if(r != rDIM-1 && grid[r+1][c] != '*'){
            if(grid[r+1][c] == 'L'){
                return false;
            }
            rRest = r;
            //Check to make sure that there is only 1
            //laser vertically placed
            for(int row = 0; row < rRest; row++){
                if(grid[row][c] == 'L'){
                    return false;
                }
            }
        }
        if(r != 0 && grid[r-1][c] != '*'){
            if(grid[r-1][c] == 'L'){
                return false;
            }
            rRest = r-1;
            //Check to make sure that there is only 1
            //laser vertically placed
            for(int row = rRest; row < rDIM; row++){
                if(grid[row][c] == 'L' && row != r){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * First, the status of the add command is displayed. If the laser was successfully placed,
     * the standard output message, followed by a new line, should be (where r and c are the
     * actual integer coordinates):
     *
     *     Laser added at: (r, c)
     *
     * If the laser could not be placed (because there was a pillar or laser at the coordinate),
     * the standard output message, followed by a new line, should be:
     *
     *     Error adding laser at: (r, c)
     * @param row - (int) the row where we want to add the laser
     * @param col - (int) the col where we want to add the laser
     * @return String value that prompts the user with whether or
     * not a laser could be placed at the specific coordinate
     */
    public String add(int row, int col){
        //If users input a value that is greater
        //than the dimension of the safe, it should
        //return an error
        if(row >= rDIM || col >= cDIM || row < 0 || col < 0){
            this.display();
            return "Error adding laser at: (" + row + ", " + col + ")";
        }
        //If the cell that we want to add it to is an outlet or is a laser beam, it should
        //return an error
        else if(grid[row][col] == 'X' || grid[row][col] == '1' || grid[row][col] == '2' ||
                grid[row][col] == '3' || grid[row][col] == '4' || grid[row][col] == '0' ||
                grid[row][col] == 'L'){
            this.display();
            return "Error adding laser at: (" + row + ", " + col + ")";
        }

        //If all condition works, add the laser at the specified location
        //then display the new graph with the laser added
        grid[row][col] = 'L';
        //South Direction
        for(int rowBeams = row+1; rowBeams < rDIM; rowBeams++){
            //If the direction that the laser is hitting hits a pillar,
            //we stop adding the laser beams
            if(grid[rowBeams][col] == '4' || grid[rowBeams][col] == '3' ||
                    grid[rowBeams][col] == '2' || grid[rowBeams][col] == '1' || grid[rowBeams][col] == '0'
                    || grid[rowBeams][col] == 'X' || grid[rowBeams][col] == 'L'){
                break;
            }
            else{
                grid[rowBeams][col] = '*';
            }
        }
        //North Direction
        for(int rowBeams = row-1; rowBeams >= 0; rowBeams--){
            if(grid[rowBeams][col] == '4' || grid[rowBeams][col] == '3' ||
                    grid[rowBeams][col] == '2' || grid[rowBeams][col] == '1' || grid[rowBeams][col] == '0'||
                    grid[rowBeams][col] == 'X' || grid[rowBeams][col] == 'L'){
                break;
            }
            else{
                grid[rowBeams][col] = '*';
            }
        }
        //East Direction
        for(int colBeams = col+1; colBeams < cDIM; colBeams++){
            if(grid[row][colBeams] == '4' || grid[row][colBeams] == '3' || grid[row][colBeams]== 'X'||
                    grid[row][colBeams] ==  'L' ||
                    grid[row][colBeams] == '2' || grid[row][colBeams] == '1' || grid[row][colBeams] == '0'){
                break;
            }
            else{
                grid[row][colBeams] = '*';
            }
        }
        //West Direction
        for(int colBeams = col-1; colBeams >= 0; colBeams--){
            if(grid[row][colBeams] == '4' || grid[row][colBeams] == '3' ||
                    grid[row][colBeams] == 'X'|| grid[row][colBeams] == 'L' ||
                    grid[row][colBeams] == '2' || grid[row][colBeams] == '1' || grid[row][colBeams] == '0'){
                break;
            }
            else{
                grid[row][colBeams] = '*';
            }
        }
        this.display();
        return "Laser added at: (" + row + ", " + col + ")" ;
    }
    //JENNIFER LIU

    // Jordan Shea
    /**
     * This function operates by removing a laser (and its beams) from
     * the user's grid, and prompting the user whether the attempt was
     * successful or not. The parameters 'row' and 'col' represent the
     * coordinates that the user would wish to see a laser removed. An
     * attempt is deemed unsuccessful if the coordinates point to an
     * index outside the length of the grid, or if the coordinate points
     * to something that is not a laser. Otherwise, this function will
     * remove the given laser and its beams (without affecting the beams
     * of other lasers that may have intersected the removed beam) and
     * create a prompt saying:
     *
     * Laser removed at: (r, c)
     *
     * @param row (int) the row where we want to remove a laser
     * @param col (int) the column where we want to remove a laser
     * @return a String value that prompts the user whether the removal
     * of the laser was successful or not at the given position
     */
    public String remove(int row, int col) {
        // If the user inputs a value that is greater than the dimension of
        // the safe, then the program should return an error message.
        if (row >= rDIM || col >= cDIM || row < 0 || col < 0) {
            this.display();
            return "Error removing laser at: (" + row + ", " + col + ")";
        }
        // If the user attempts to remove an object that is not a laser, then
        // the program should return an error message.
        else if (grid[row][col] != 'L') {
            this.display();
            return "Error: No laser exists at: (" + row + ", " + col + ")";
        } else {
            boolean initialRowCheck = true;
            boolean initialColCheck = true;
            int rowCounter = 0;
            int colCounter = 0;
            for (int initRow = 0; initRow < rDIM; initRow++){
                if (grid[initRow][col] == 'L'){
                    colCounter++;
                }
            }
            for (int initCol = 0; initCol < cDIM; initCol++){
                if (grid[row][initCol] == 'L'){
                    rowCounter++;
                }
            }
            // Removes laser from the current position by substituting it with a '.'
            if (rowCounter >= 2){
                initialRowCheck = false;
            }
            if (colCounter >= 2){
                initialColCheck = false;
            }
            if (initialRowCheck && initialColCheck){
                grid[row][col] = '.';
            }
            else {
                grid[row][col] = '*';
            }
            // Begins the process of removing vertical beams
            for (int rowBeams = 0; rowBeams < rDIM; rowBeams++) {
                if (rowBeams != row && grid[rowBeams][col] == '*') {
                    boolean check = true;
                    for (int newCol = 0; newCol < cDIM; newCol++) {
                        // Checks for intersecting lasers
                        if (grid[rowBeams][newCol] == 'L') {
                            check = false;
                        }
                    }
                    // Removes beam by replacing '*' with '.'
                    if (check) {
                        grid[rowBeams][col] = '.';
                    }
                    // Does not remove beam if it can be associated with a
                    // separate laser
                    else {
                        grid[rowBeams][col] = '*';
                    }
                }

            }
            // Begins the process of removing horizontal beams
            for (int colBeams = 0; colBeams < cDIM; colBeams++) {
                if (colBeams != col && grid[row][colBeams] == '*') {
                    boolean check = true;
                    for (int newRow = 0; newRow < cDIM; newRow++) {
                        if (grid[newRow][colBeams] == 'L') {
                            check = false;
                        }
                    }
                    // Removes beam by replacing '*' with '.'
                    if (check) {
                        grid[row][colBeams] = '.';
                        // Does not remove beam if it can be associated with a
                        // separate laser
                    }
                    else {
                        grid[row][colBeams] = '*';
                    }
                }
            }
            if (!initialColCheck) {
                for (int endRow = row + 1; endRow < rDIM; endRow++) {
                    if (grid[endRow][col] == '0' || grid[endRow][col] == '1' ||
                            grid[endRow][col] == '2' || grid[endRow][col] == '3' ||
                            grid[endRow][col] == '4' || grid[endRow][col] == 'X') {
                        endRow = rDIM;
                    } else {
                        if (grid[endRow][col] != 'L') {
                            grid[endRow][col] = '*';
                        }
                    }
                }
                for (int endRow = row - 1; endRow > 0; endRow--) {
                    if (grid[endRow][col] == '0' || grid[endRow][col] == '1' ||
                            grid[endRow][col] == '2' || grid[endRow][col] == '3' ||
                            grid[endRow][col] == '4' || grid[endRow][col] == 'X') {
                        endRow = 0;
                    } else {
                        if (grid[endRow][col] != 'L') {
                            grid[endRow][col] = '*';
                        }
                    }
                }
            }
            if (!initialRowCheck){
                for (int endCol = col + 1; endCol < cDIM; endCol++) {
                    if (grid[row][endCol] == '0' || grid[row][endCol] == '1' ||
                            grid[row][endCol] == '2' || grid[row][endCol] == '3' ||
                            grid[row][endCol] == '4' || grid[row][endCol] == 'X') {
                        endCol = rDIM;
                    }
                    else {
                        if (grid[row][endCol] != 'L') {
                            grid[row][endCol] = '*';
                        }
                    }
                }
                for (int endCol = col - 1; endCol > 0; endCol--) {
                    if (grid[row][endCol] == '0' || grid[row][endCol] == '1' ||
                            grid[row][endCol] == '2' || grid[row][endCol] == '3' ||
                            grid[row][endCol] == '4' || grid[row][endCol] == 'X') {
                        endCol = 0;
                    }
                    else {
                        if (grid[row][endCol] != 'L') {
                            grid[row][endCol] = '*';
                        }
                    }
                }
            }
        }

        // Displays updated grid with the position in which the laser was removed
        this.display();
        return "Laser removed at: (" + row + ", " + col + ")";
    }

    /**
     * This is a simple method that closes the program, which
     * is invoked if the user typed in the 'q' command.
     *
     * Pre-Conditions: None
     * Post-Conditions: Program ends
     */
    public void quit(){
        System.exit(2);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 1) {
            //MOSES LAGOON
            LasersPTUI lasers = new LasersPTUI(args[0]);
            lasers.display(); //PRINTING DISPLAY HERE
            System.out.print("> ");
            Scanner sc = new Scanner(System.in);
            lasers.commands(sc.nextLine());
            while(sc.hasNextLine()){
                lasers.commands(sc.nextLine());
            }
            //MOSES LAGOON
        } else if (args.length == 2) {
            //MOSES LAGOON
            //Creating a new lasers object to print out display
            LasersPTUI lasers = new LasersPTUI(args[0]);
            // Jordan Shea
            Scanner sc = new Scanner(new File(args[1]));
            lasers.display();
            System.out.print("> ");
            while (sc.hasNext()){
                String line = sc.nextLine();
                System.out.println(line);
                lasers.commands(line);
            }
            Scanner scnInput = new Scanner(System.in);
            lasers.commands(scnInput.nextLine());
            while(scnInput.hasNextLine()){
                lasers.commands(scnInput.nextLine());
            }
            //MOSES LAGOON
        } else {
            System.out.println("Usage: java LasersPTUI safe-file [input]");
        }
    }
}
