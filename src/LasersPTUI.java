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


    }

    public void display(){


        String result = "  ";
        /** Printing Column Numbers */
        for (int c = 0; c<cDIM; c++){
            result += " " + c;        //Prints out Column number here
        }
        result+= "\n" + "   ";

        /** TOP ROW DASES "-------" */
        for(int j = 1; j<cDIM; j++){
            result += "--";         //TOP ROW DASHES HERE
        }
        result += "-\n";            //ONE MORE DASH + NEWLINE

        /** DATA BODY - rows of them */
        for (int r =0; r <rDIM; r++){
            result += r + "|";                  // ROW Number and Left side bars
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

    //MOSES LAGOON

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
//        System.out.println("My name is Jordan Shea");
//        System.out.println("My name is Jennifer Liu");
//        System.out.println("My name is Moses Lagoon");
//        System.out.println("Our project account is p142-03n");
        // Jordan Shea
        try {
            if (args.length == 1) {
                //MOSES LAGOON
                LasersPTUI lasers = new LasersPTUI(args[0]);
                lasers.display();           //PRINTING DISPLAY HERE
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

        } catch (Exception e){
            System.out.println(e);
        }
    }
}
