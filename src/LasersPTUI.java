import java.io.FileNotFoundException;

/**
 * Created by Jordan on 4/9/2016.
 *
 */
public class LasersPTUI {


    public static void main(String[] args) throws FileNotFoundException{
        if(args.length != 0){
            System.out.println("Usage: java LasersPTUI safe-file" + args[0]);
        }
    }
}
