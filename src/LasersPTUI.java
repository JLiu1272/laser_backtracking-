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
//    private char[][] grid;    // safe
//
//    //MOSES LAGOON - Constructor for LasersPTUI
//
//    public int rDIM;    //row dimension of the safe
//    public int cDIM;    //column dimension of the safe

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 1) {
            //MOSES LAGOON
            LasersConfig lasers = new LasersConfig(args[0]);
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
            Scanner sc = new Scanner(new File(args[1]));
            LasersConfig lasers = new LasersConfig(args[0]);
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
