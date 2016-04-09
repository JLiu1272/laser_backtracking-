import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.FileNotFoundException;

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
    private char[][] grid;

    public LasersPTUI(){

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
    }
}
