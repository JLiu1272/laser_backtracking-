import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The full representation of a configuration. It can read an in an initial conf-
 * guartion. 
 */

public class LasersConfig {
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
    public LasersConfig(String filename) throws FileNotFoundException {
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
    public LasersConfig(LasersConfig otherSafe){
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
        if(ch[0].equals("")){
            System.out.print("> ");

        }
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
                    System.out.println(add(digits.get(0), digits.get(1)));
                    this.display();
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
            case ' ':
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
                    this.display();
                }
                System.out.print("> ");
                break;
            case 'v':
                System.out.println(verify());
                this.display();
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
                //If there is a power outlet, make sure that the number of lasers
                //surrounding the outlet matches with its number
                //In here, we call the checkNeighbors method to help us do this
                else if(grid[row][col] == '0'){
                    //put conditions
                    if(!checkNeighbors(0, row, col,'L')){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }

                }
                else if(grid[row][col] == '1'){
                    //put condition
                    if(!checkNeighbors(1, row, col, 'L')){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }

                }
                else if(grid[row][col] == '2'){
                    //put condition
                    if(!checkNeighbors(2, row, col, 'L')){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }
                }
                else if(grid[row][col] == '3'){
                    //put condition
                    if(!checkNeighbors(3, row, col, 'L')){
                        return "Error verifying at: (" + row + ", " + col + ")";
                    }
                }
                else if(grid[row][col] == '4'){
                    //put condition
                    if(!checkNeighbors(4, row, col, 'L')){
                        return "Error verifying at: (" + row + ", " + col + ")";
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
    public boolean checkNeighbors(int num, int row, int col, char val){
        int numLasers = 0;
        if(row == 0 && col == 0){
            if(grid[row][col+1] == val){
                numLasers++;
            }
            if(grid[row+1][col] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checks for the top right corner (0, cDIM)
        else if(row == 0 && col == cDIM-1){
            if(grid[row][col-1] == val){
                numLasers++;
            }
            if(grid[row+1][col] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checks for the bottom left corner (0, rDIM)
        else if(row == rDIM -1 && col == 0){
            if(grid[row-1][col] == val){
                numLasers++;
            }
            if(grid[row][col+1] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checks for the bottom right corner (rDIM, cDIM)
        else if(row == rDIM-1 && col == cDIM-1){
            if(grid[row][col-1] == val){
                numLasers++;
            }
            if(grid[row-1][col] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Checking for the sides
        //The left side
        else if(col == 0 && row > 0){
            if(grid[row-1][col] == val){
                numLasers++;
            }
            if(grid[row+1][col] == val){
                numLasers++;
            }
            if(grid[row][col+1] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for Top Side
        else if(col > 0 && row == 0){
            if(grid[row+1][col] == val){
                numLasers++;
            }
            if(grid[row][col-1] == val){
                numLasers++;
            }
            if(grid[row][col+1] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for the Right Side
        else if(col == cDIM-1 && row > 0){
            if(grid[row+1][col] == val){
                numLasers++;
            }
            if(grid[row][col-1] == val){
                numLasers++;
            }
            if(grid[row-1][col] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for the bottom Side
        else if(row == rDIM-1 && col > 0){
            if(grid[row-1][col] == val){
                numLasers++;
            }
            if(grid[row][col-1] == val){
                numLasers++;
            }
            if(grid[row][col+1] == val){
                numLasers++;
            }
            if(numLasers != num){
                return false;
            }
        }
        //Check for the cells that are inside the
        //grid
        else{
            if(grid[row-1][col] == val){
                numLasers++;
            }
            if(grid[row][col-1] == val){
                numLasers++;
            }
            if(grid[row][col+1] == val){
                numLasers++;
            }
            if(grid[row+1][col] == val){
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
     * @return boolean value that checks whether or not lasers
     * are in sight of one another
     */
    public boolean verifyWithPos(int r, int c){

        //Making sure that the column is not 0
        if(c != 0){
            for(int col = c-1; col >= 0; col--){
                if(grid[r][col] != '*'){
                    if(grid[r][col] == 'L'){
                        return false;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        //Making sure that the col is not greater than
        //the column dimension
        if(c != cDIM-1){
            for(int col = c+1; col < cDIM; col++){
                if(grid[r][col] != '*'){
                    if(grid[r][col] == 'L'){
                        return false;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        //Row does not equal to 0
        if(r != 0){
            for(int row = r-1; row >= 0; row--){
                if(grid[row][c] != '*'){
                    if(grid[row][c] == 'L'){
                        return false;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        //Row does not equal to the row dimension
        if(r != rDIM-1){
            for(int row = r+1; row < cDIM; row++){
                //If I hit something that is not
                //a laser beam, it means I have hit
                //a pillar or a laser. If it is a
                //pillar, I break out of the for
                //loop, else I return false
                if(grid[row][c] != '*'){
                    if(grid[row][c] == 'L'){
                        return false;
                    }
                    else{
                        break;
                    }
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
            return "Error adding laser at: (" + row + ", " + col + ")";
        }
        //If the cell that we want to add it to is an outlet or is a laser beam, it should
        //return an error
        else if(grid[row][col] == 'X' || grid[row][col] == '1' || grid[row][col] == '2' ||
                grid[row][col] == '3' || grid[row][col] == '4' || grid[row][col] == '0' ||
                grid[row][col] == 'L'){
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
            return "Error removing laser at: (" + row + ", " + col + ")";
        }
        // If the user attempts to remove an object that is not a laser, then
        // the program should return an error message.
        else if (grid[row][col] != 'L') {
            return "Error removing laser at: (" + row + ", " + col + ")";
        }
        else {
            // Local Variables
            boolean laserCheck = false;
            // Corresponds to beams being shot to the right
            int right;
            if (col == cDIM - 1){
                right = col;
            }
            else {
                right = col + 1;
            }
            // Corresponds to beams being shot to the left
            int left;
            if (col == 0){
                left = col;
            }
            else {
                left = col - 1;
            }
            // Corresponds to beams being shot upwards
            int up;
            if (row == 0){
                up = row;
            }
            else {
                up = row - 1;
            }
            // Corresponds with beams being shot downwards
            int down;
            if (row == rDIM - 1){
                down = row;
            }
            else {
                down = row + 1;
            }
            int counter;
            // Removes the beam that is being shot to the right
            counter = 0;
            for (int east = right; east <= cDIM -1; east++){
                counter++;
                if (grid[row][east] == '0' || grid[row][east] == '1' ||
                        grid[row][east] == '2' || grid[row][east] == '3' ||
                        grid[row][east] == '4' || grid[row][east] == 'X'){
                    east = cDIM - 1;
                }
                else if (grid[row][east] == 'L' && counter != 1){
                    laserCheck = true;
                    east = cDIM - 1;
                }
                else {
                    grid[row][east] = '.';
                }
                for (int north = up; north >= 0; north--){
                    if (grid[north][east] == '0' || grid[north][east] == '1' ||
                            grid[north][east] == '2' || grid[north][east] == '3' ||
                            grid[north][east] == '4' || grid[north][east] == 'X'){
                        north = 0;
                    }
                    if (grid[north][east] == 'L'){
                        grid[row][east] = '*';
                        north = 0;
                    }
                }
                for (int south = down; south <= rDIM -1; south++){
                    if (grid[south][east] == '0' || grid[south][east] == '1' ||
                            grid[south][east] == '2' || grid[south][east] == '3' ||
                            grid[south][east] == '4' || grid[south][east] == 'X'){
                        south = rDIM - 1;
                    }
                    if (grid[south][east] == 'L'){
                        grid[row][east] = '*';
                        south = rDIM - 1;
                    }
                }
            }
            // Removes the beam that is being shot to the left
            counter = 0;
            for (int west = left; west >= 0; west--){
                counter++;
                if (grid[row][west] == '0' || grid[row][west] == '1' ||
                        grid[row][west] == '2' || grid[row][west] == '3' ||
                        grid[row][west] == '4' || grid[row][west] == 'X'){
                    west = 0;
                }
                else if (grid[row][west] == 'L' && counter != 1){
                    laserCheck = true;
                    west = 0;
                }
                else {
                    grid[row][west] = '.';
                }
                for (int north = up; north >= 0; north--){
                    if (grid[north][west] == '0' || grid[north][west] == '1' ||
                            grid[north][west] == '2' || grid[north][west] == '3' ||
                            grid[north][west] == '4' || grid[north][west] == 'X'){
                        north = 0;
                    }
                    if (grid[north][west] == 'L'){
                        grid[row][west] = '*';
                        north = 0;
                    }
                }
                for (int south = down; south <= rDIM - 1; south++){
                    if (grid[south][west] == '0' || grid[south][west] == '1' ||
                            grid[south][west] == '2' || grid[south][west] == '3' ||
                            grid[south][west] == '4' || grid[south][west] == 'X'){
                        south = rDIM - 1;
                    }
                    if (grid[south][west] == 'L'){
                        grid[row][west] = '*';
                        south = rDIM - 1;
                    }
                }
            }
            // Removes the beam that is being shot downwards
            counter = 0;
            for (int south = down; south <= rDIM - 1; south++){
                counter++;
                if (grid[south][col] == '0' || grid[south][col] == '1' ||
                        grid[south][col] == '2' || grid[south][col] == '3' ||
                        grid[south][col] == '4' || grid[south][col] == 'X'){
                    south = rDIM - 1;
                }
                else if (grid[south][col] == 'L' && counter != 1){
                    laserCheck = true;
                    south = rDIM - 1;
                }
                else {
                    grid[south][col] = '.';
                }
                for (int west = left; west >= 0; west--){
                    if (grid[south][west] == '0' || grid[south][west] == '1' ||
                            grid[south][west] == '2' || grid[south][west] == '3' ||
                            grid[south][west] == '4' || grid[south][west] == 'X'){
                        west = 0;
                    }
                    if (grid[south][west] == 'L'){
                        grid[south][col] = '*';
                        west = 0;
                    }
                }
                for (int east = right; east <= cDIM - 1; east++){
                    if (grid[south][east] == '0' || grid[south][east] == '1' ||
                            grid[south][east] == '2' || grid[south][east] == '3' ||
                            grid[south][east] == '4' || grid[south][east] == 'X'){
                        east = cDIM - 1;
                    }
                    if (grid[south][east] == 'L'){
                        grid[south][col] = '*';
                        east = cDIM - 1;
                    }
                }
            }
            // Removes the beam that is being shot upwards
            counter = 0;
            for (int north = up; north >= 0; north--){
                counter++;
                if (grid[north][col] == '0' || grid[north][col] == '1' ||
                        grid[north][col] == '2' || grid[north][col] == '3' ||
                        grid[north][col] == '4' || grid[north][col] == 'X'){
                    north = 0;
                }
                else if (grid[north][col] == 'L' && counter != 1){
                    laserCheck = true;
                    north = 0;
                }
                else {
                    grid[north][col] = '.';
                }
                for (int west = left; west >= 0; west--){
                    if (grid[north][west] == '0' || grid[north][west] == '1' ||
                            grid[north][west] == '2' || grid[north][west] == '3' ||
                            grid[north][west] == '4' || grid[north][west] == 'X'){
                        west = 0;
                    }
                    if (grid[north][west] == 'L'){
                        grid[north][col] = '*';
                        west = 0;
                    }
                }
                for (int east = right; east <= cDIM - 1; east++){
                    if (grid[north][east] == '0' || grid[north][east] == '1' ||
                            grid[north][east] == '2' || grid[north][east] == '3' ||
                            grid[north][east] == '4' || grid[north][east] == 'X'){
                        east = cDIM - 1;
                    }
                    if (grid[north][east] == 'L'){
                        grid[north][col] = '*';
                        east = cDIM - 1;
                    }
                }
            }
            // Determines what symbol the old laser should be replaced with
            if (laserCheck){
                grid[row][col] = '*';
            }
            else {
                grid[row][col] = '.';
            }
        }
        // Displays updated grid with the position in which the laser was removed
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


}