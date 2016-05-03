package model;

import com.sun.xml.internal.bind.v2.TODO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

public class LasersModel extends Observable {

    private char[][] grid;
    private int rDIM;
    private int cDIM;
    private int userRow;
    private int userCol;
    private String userCommand;
    private boolean commandError;
    private boolean coordinateError;
    private boolean addSuccess;
    private boolean addFailure;
    private boolean removeSuccess;
    private boolean removeFailure;
    private boolean verifySuccess;
    private boolean verifyFailure;
    private boolean display;
    private boolean help;
    private boolean quit;


    public LasersModel(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename)); //scanning the file in
        this.userRow = 0;
        this.userCol = 0;
        this.userCommand = "";
        this.commandError = false;
        this.coordinateError = false;
        this.addSuccess = false;
        this.addFailure = false;
        this.removeSuccess = false;
        this.removeFailure = false;
        this.verifySuccess = false;
        this.verifyFailure = false;
        this.display = false;
        this.help = false;
        this.quit = false;
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

    public int getrDIM(){
        return this.rDIM;
    }

    public int getcDIM(){
        return this.cDIM;
    }

    public char[][] getGrid(){
        return this.grid;
    }

    public boolean getCommandError(){
        return this.commandError;
    }

    public boolean getCoordinateError(){
        return this.coordinateError;
    }

    public String getUserCommand(){
        return this.userCommand;
    }

    public int getUserRow(){
        return this.userRow;
    }

    public int getUserCol(){
        return this.userCol;
    }

    public boolean getAddSuccess(){
        return this.addSuccess;
    }

    public boolean getRemoveSuccess(){
        return this.removeSuccess;
    }

    public boolean getAddFailure(){
        return this.addFailure;
    }

    public boolean getRemoveFailure(){
        return this.removeFailure;
    }

    public boolean getDisplay() { return this.display; }

    public boolean getHelp() { return this.help; }

    public boolean getQuit() { return this.quit; }

    public boolean getVerifySuccess() { return this.verifySuccess; }

    public boolean getVerifyFailure() { return this.verifyFailure; }

    public void commands(String str){
        this.commandError = false;
        this.coordinateError = false;
        this.addSuccess = false;
        this.addFailure = false;
        this.removeSuccess = false;
        this.removeFailure = false;
        this.verifySuccess = false;
        this.verifyFailure = false;
        this.display = false;
        this.help = false;
        this.quit = false;
        //Splits the string base on spaces
        String[] ch = str.split("\\s+");
        ArrayList<Integer> digits = new ArrayList<>();
        //Only care about the first character of the
        //first string
        if(ch[0].equals("")){
            return;
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
                    this.coordinateError = true;
                } else {
                    this.userRow = digits.get(0);
                    this.userCol = digits.get(1);
                    add(this.userRow, this.userCol);
                }
                break;
            case 'd':
                this.display = true;
                break;
            case 'h':
                this.help = true;
                break;
            case 'q':
                this.quit = true;
                break;
            case 'r':
                if (digits.size() < 2 || digits.size() > 2) {
                    this.coordinateError = true;
                } else {
                    this.userRow = digits.get(0);
                    this.userCol = digits.get(1);
                    remove(this.userRow, this.userCol);
                }
                break;
            case 'v':
                verify();
                break;
            default:
                this.commandError = true;
                this.userCommand = str;
                break;
        }
        //Do the actions that correspond to the command
        //Return error if the command is not valid
        announceChange();
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
     */
    public void add(int row, int col){
        //If users input a value that is greater
        //than the dimension of the safe, it should
        //return an error
        if(row >= rDIM || col >= cDIM || row < 0 || col < 0){
            this.addFailure = true;
            return;
        }
        //If the cell that we want to add it to is an outlet or is a laser beam, it should
        //return an error
        else if(grid[row][col] == 'X' || grid[row][col] == '1' || grid[row][col] == '2' ||
                grid[row][col] == '3' || grid[row][col] == '4' || grid[row][col] == '0' ||
                grid[row][col] == 'L'){
            this.addFailure = true;
            return;
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
        this.addSuccess = true;
    }

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
     */
    public void remove(int row, int col) {
        // If the user inputs a value that is greater than the dimension of
        // the safe, then the program should return an error message.
        if (row >= rDIM || col >= cDIM || row < 0 || col < 0) {
            this.removeFailure = true;
            return;
        }
        // If the user attempts to remove an object that is not a laser, then
        // the program should return an error message.
        else if (grid[row][col] != 'L') {
            this.removeFailure = true;
            return;
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
                    break;
                }
                else if (grid[row][east] == 'L' && counter != 1){
                    laserCheck = true;
                    break;
                }
                else {
                    grid[row][east] = '.';
                }
                for (int north = up; north >= 0; north--){
                    if (grid[north][east] == '0' || grid[north][east] == '1' ||
                            grid[north][east] == '2' || grid[north][east] == '3' ||
                            grid[north][east] == '4' || grid[north][east] == 'X'){
                        break;
                    }
                    if (grid[north][east] == 'L'){
                        grid[row][east] = '*';
                        break;
                    }
                }
                for (int south = down; south <= rDIM -1; south++){
                    if (grid[south][east] == '0' || grid[south][east] == '1' ||
                            grid[south][east] == '2' || grid[south][east] == '3' ||
                            grid[south][east] == '4' || grid[south][east] == 'X'){
                        break;
                    }
                    if (grid[south][east] == 'L'){
                        grid[row][east] = '*';
                        break;
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
                    break;
                }
                else if (grid[row][west] == 'L' && counter != 1){
                    laserCheck = true;
                    break;
                }
                else {
                    grid[row][west] = '.';
                }
                for (int north = up; north >= 0; north--){
                    if (grid[north][west] == '0' || grid[north][west] == '1' ||
                            grid[north][west] == '2' || grid[north][west] == '3' ||
                            grid[north][west] == '4' || grid[north][west] == 'X'){
                        break;
                    }
                    if (grid[north][west] == 'L'){
                        grid[row][west] = '*';
                        break;
                    }
                }
                for (int south = down; south <= rDIM - 1; south++){
                    if (grid[south][west] == '0' || grid[south][west] == '1' ||
                            grid[south][west] == '2' || grid[south][west] == '3' ||
                            grid[south][west] == '4' || grid[south][west] == 'X'){
                        break;
                    }
                    if (grid[south][west] == 'L'){
                        grid[row][west] = '*';
                        break;
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
                    break;
                }
                else if (grid[south][col] == 'L' && counter != 1){
                    laserCheck = true;
                    break;
                }
                else {
                    grid[south][col] = '.';
                }
                for (int west = left; west >= 0; west--){
                    if (grid[south][west] == '0' || grid[south][west] == '1' ||
                            grid[south][west] == '2' || grid[south][west] == '3' ||
                            grid[south][west] == '4' || grid[south][west] == 'X'){
                        break;
                    }
                    if (grid[south][west] == 'L'){
                        grid[south][col] = '*';
                        break;
                    }
                }
                for (int east = right; east <= cDIM - 1; east++){
                    if (grid[south][east] == '0' || grid[south][east] == '1' ||
                            grid[south][east] == '2' || grid[south][east] == '3' ||
                            grid[south][east] == '4' || grid[south][east] == 'X'){
                        break;
                    }
                    if (grid[south][east] == 'L'){
                        grid[south][col] = '*';
                        break;
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
                    break;
                }
                else if (grid[north][col] == 'L' && counter != 1){
                    laserCheck = true;
                    break;
                }
                else {
                    grid[north][col] = '.';
                }
                for (int west = left; west >= 0; west--){
                    if (grid[north][west] == '0' || grid[north][west] == '1' ||
                            grid[north][west] == '2' || grid[north][west] == '3' ||
                            grid[north][west] == '4' || grid[north][west] == 'X'){
                        break;
                    }
                    if (grid[north][west] == 'L'){
                        grid[north][col] = '*';
                        break;
                    }
                }
                for (int east = right; east <= cDIM - 1; east++){
                    if (grid[north][east] == '0' || grid[north][east] == '1' ||
                            grid[north][east] == '2' || grid[north][east] == '3' ||
                            grid[north][east] == '4' || grid[north][east] == 'X'){
                        break;
                    }
                    if (grid[north][east] == 'L'){
                        grid[north][col] = '*';
                        break;
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
        this.removeSuccess = true;
    }

    /**
     * Verify command displays a status message that indicates whether the safe is
     * valid or not. In order to be valid, none of the rules of the safe may be
     * violated. Each tile that is not a pillar must have either a laser or beam
     * covering it. Each pillar that requires a certain number of neighboring lasers
     * must add up exactly. If two or more lasers in the sight of each other,
     * in the cardinal direction, it is invalid
     *
     */
    public void verify(){
        for(int row = 0; row < rDIM; row++){
            for(int col = 0; col < cDIM; col++){
                //If the current object at this position is a Laser,
                //call verifyWithPos
                //If one of the tiles are empty, return an error
                if(grid[row][col] == '.'){
                    this.verifyFailure = true;
                    this.userRow = row;
                    this.userCol = col;
                }
                //If there are more than one laser in the same row or column,
                //return false
                if(grid[row][col] == 'L'){
                    if(!verifyWithPos(row,col)){
                        this.verifyFailure = true;
                        this.userRow = row;
                        this.userCol = col;
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
                        this.verifyFailure = true;
                        this.userRow = row;
                        this.userCol = col;
                    }

                }
                else if(grid[row][col] == '1'){
                    //put condition
                    if(!checkNeighbors(1, row, col, 'L')){
                        this.verifyFailure = true;
                        this.userRow = row;
                        this.userCol = col;
                    }

                }
                else if(grid[row][col] == '2'){
                    //put condition
                    if(!checkNeighbors(2, row, col, 'L')){
                        this.verifyFailure = true;
                        this.userRow = row;
                        this.userCol = col;
                    }
                }
                else if(grid[row][col] == '3'){
                    //put condition
                    if(!checkNeighbors(3, row, col, 'L')){
                        this.verifyFailure = true;
                        this.userRow = row;
                        this.userCol = col;
                    }
                }
                else if(grid[row][col] == '4'){
                    //put condition
                    if(!checkNeighbors(4, row, col, 'L')){
                        this.verifyFailure = true;
                        this.userRow = row;
                        this.userCol = col;
                    }
                }
            }
        }
        this.verifySuccess = true;
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
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    private void announceChange() {
        setChanged();
        notifyObservers();
    }
}
