import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.FileNotFoundException;

/**
 * @author Moses Lagoon
 *
 */
public class LasersPTUI {


    public static void main(String[] args) throws FileNotFoundException{
        if(args.length != 0){
            System.out.println("Usage: java LasersPTUI safe-file" + args[0]);
        }
        System.out.println("Hello, there!");
        System.out.println("Next checkin!");
    }
}
