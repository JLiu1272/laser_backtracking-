import com.sun.org.apache.xpath.internal.SourceTree;

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
 */

public class LasersPTUI {
    private char[][] grid;    // safe

    //MOSES LAGOON - Constructor for LasersPTUI

    public int rDIM;    //row dimension of the safe
    public int cDIM;    //column dimension of the safe
    private int[] rowArray;
    private int[] colArray;


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

        for (int r =0; r <rDIM; r++){
            for(int c = 0; c < cDIM; c++){
                String s = in.next();
                System.out.print(this.grid[r][c]);
            }
            System.out.println();
        }
    }

    public String display(){
        return "";
    }

    public boolean verify(){
        return false;
    }

    public char[][] remove(int r, int c){
        return null;
    }

    public String add(int r, int c){
        return "";
    }

    public static void main(String[] args) {
        System.out.println("My name is Jordan Shea");
        System.out.println("My name is Jennifer Liu");
        System.out.println("My name is Moses Lagoon");
        System.out.println("Our project account is p142-03n");
        // Jordan Shea
        try {
            if (args.length == 1) {
                new LasersPTUI(args[0]);
            } else if (args.length == 2) {
                new LasersPTUI(args[0]);
                // Need to create scanner/helper method to read in args[1]
            } else {
                System.out.println("Usage: java LasersPTUI safe-file [input]");
            }
            
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
