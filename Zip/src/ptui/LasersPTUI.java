package ptui;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import model.LasersModel;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author Jordan Edward Shea
 * @author Jennifer Liu
 * @author Moses Lagoon
 */
public class LasersPTUI implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
        // Jordan Shea
        displayGrid();
        System.out.print("> ");
    }

    public LasersModel getModel() { return this.model; }

    @Override
    public void update(Observable o, Object arg) {
        displayOutput();
    }

    // Jordan Shea
    public void displayGrid(){
        String result = "  ";
        /*Printing Column Numbers */
        for (int c = 0; c < model.getcDIM(); c++){
            result += " " + c%10;        //Prints out Column number here
        }
        result+= "\n" + "   ";

        /** TOP ROW DASHES "-------" */
        for(int j = 1; j < model.getcDIM(); j++){
            result += "--";         //TOP ROW DASHES HERE
        }
        result += "-\n";            //ONE MORE DASH + NEWLINE

        /** DATA BODY - rows of them */
        for (int r =0; r < model.getrDIM(); r++){
            result += r%10 + "|";                  // ROW Number and Left side bars
            for(int c = 0; c < model.getcDIM(); c++){
                result += " " + model.getGrid()[r][c];    //filling the grid in
            }
            result += "\n";
        }
        System.out.print(result);
    }

    // Jordan Shea
    public void displayOutput(){
        if (model.getAddSuccess()) {
            System.out.println("Laser added at: (" + model.getUserRow() + ", " + model.getUserCol()+ ")");
            displayGrid();
        }
        else if (model.getAddFailure()){
            System.out.println("Error adding laser at: (" + model.getUserRow() + ", " + model.getUserCol() + ")");
            displayGrid();
        }
        else if (model.getDisplay()){
            displayGrid();
        } else if (model.getHelp()) {
            System.out.print(
                            "a|add r c: Add laser to (r,c)\n" +
                            "d|display: Display safe\n" +
                            "h|help: Print this help message\n" +
                            "q|quit: Exit program\n" +
                            "r|remove r c: Remove laser from (r,c)\n" +
                            "v|verify: Verify safe correctness\n");
        }
        else if (model.getQuit()){
            System.exit(2);
        }
        else if (model.getVerifySuccess()){
            System.out.println("Safe is fully verified!");
            displayGrid();
        }
        else if (model.getVerifyFailure()){
            System.out.println("Error verifying at: (" + model.getUserRow() + ", " + model.getUserCol() + ")");
            displayGrid();
        }
        else if (model.getRemoveSuccess()) {
            System.out.println("Laser removed at: (" + model.getUserRow() + ", " + model.getUserCol()+ ")");
            displayGrid();
        }
        else if (model.getRemoveFailure()) {
            System.out.println("Error removing laser at: (" + model.getUserRow() + ", " + model.getUserCol() + ")");
            displayGrid();
        }
        else if (model.getCommandError()) {
            System.out.println("Unrecognized command: " + model.getUserCommand());
            displayGrid();
        }
        else if (model.getCoordinateError()) {
            System.out.println("Incorrect coordinates");
            displayGrid();
        }
        System.out.print("> ");
    }
}
