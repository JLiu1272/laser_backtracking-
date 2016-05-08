package gui;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import com.sun.javafx.font.t2k.T2KFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import model.*;
import ptui.LasersPTUI;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Jordan Edward Shea
 * @author Jennifer Liu
 * @author Moses Lagoon
 */
public class LasersGUI extends Application implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;
    private GridPane grid;
    private Button[][] referenceGrid;
    private Label message;

    private int notVerifiedRow;
    private int getNotVerifiedCol;

    private final Stage stage = new Stage();    //NEW STAGE??

    /** this can be removed - it is used to demonstrates the button toggle */
    private static boolean status = true;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This is a private demo method that shows how to create a button
     * and attach a foreground image with a background image that
     * toggles from yellow to red each time it is pressed.
     *
     * @param stage the stage to add components into
     */
    private void buttonDemo(Stage stage) {
        // this demonstrates how to create a button and attach a foreground and
        // background image to it.
        Button button = new Button();
        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        setButtonBackground(button, "yellow.png");
        button.setOnAction(e -> {
            // toggles background between yellow and red
            if (!status) {
                setButtonBackground(button, "yellow.png");
            } else {
                setButtonBackground(button, "red.png");
            }
            status = !status;
        });

        Scene scene = new Scene(button);
        stage.setScene(scene);
    }

    /**
     * The
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        // TODO
        buttonDemo(stage);  // this can be removed/altered
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO
       // init(primaryStage);  // do all your UI initialization here
        BorderPane border = new BorderPane();
        Scene scene = new Scene(border);
        border.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,CornerRadii.EMPTY, Insets.EMPTY)));


        border.setTop(topMessagePane());       //TOP of the borderPane
        this.grid = centerButtonPane();
        border.setCenter(this.grid);  //CENTER GridButtons
        border.setBottom(bottombtns());        //BOTTOM Buttons

        primaryStage.setResizable(false);
        primaryStage.setTitle("Lasers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The Message status label for the top. Flowpane is used.
     * @return FlowPane
     */
    private FlowPane topMessagePane(){
        FlowPane topLabel = new FlowPane();
        message = new Label();
        message.setText("Message: Status of safe!");
        topLabel.setAlignment(Pos.CENTER);
        topLabel.getChildren().add(message);
        return topLabel;

        // Message types:
        // The name of the safe file when initially loaded or restarted.
        // The result of attempting to add or remove a laser from the safe.
        // The status of the safe when checked for correctness
        // The result of requesting a hint for the next laser to place.
        // The result of attempting to fully solve the laser placements.
    }


    /**
     * CenterButton pane for the safe
     * @return GridPane
     */
    private GridPane centerButtonPane(){
        grid = new GridPane();

        int row;
        int col;
        int rDIM = model.getrDIM();             //getter of rows: rDIM model
        int cDIM = model.getcDIM();             //getter of cols: cDIM model
        char[][] safe = model.getGrid();
        referenceGrid = new Button[rDIM][cDIM];

        for(char[] c: safe){
            System.out.println(Arrays.toString(c));
        }


        for(row = 0; row<rDIM; row++){
            for (col = 0; col<cDIM; col++){
              //  Button btn = new Button();
                Button button = new Button();
                setImage(safe[row][col], button);
                int r = row;
                int c = col;
                referenceGrid[row][col] = button;
                grid.add(button,col, row);
                final int r1 = row;
                final int c1 = col;
                button.setOnAction(event -> {
                    if(model.getGrid()[r1][c1] != 'L'){
                        setImage(model.getGrid()[notVerifiedRow][getNotVerifiedCol],referenceGrid[notVerifiedRow][getNotVerifiedCol]);
                        model.add(r, c);
                        if(model.getAddFailure()){
                            message.setText("Error adding model at: (" + r + ", " + c + ")");
                        }
                        else if(model.getAddSuccess()){
                            message.setText("Laser added at: (" + r + ", " + c + ")");
                        }
                    }
                    else{
                        setImage(model.getGrid()[notVerifiedRow][getNotVerifiedCol],referenceGrid[notVerifiedRow][getNotVerifiedCol]);
                        model.remove(r, c);
                        if(model.getRemoveFailure()){
                            message.setText("Error removing model at: (" + r + ", " + c + ")");
                        }
                        else if(model.getRemoveSuccess()){
                            message.setText("Laser removed at: (" + r + ", " + c + ")");
                        }
                    }
                });

            }
        }
        grid.setGridLinesVisible(false);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    /**
     * This method is responsible for setting the image icon in the respective
     * grid buttons as read from the file.
     *
     * @param safeContent   The contents in the safe
     * @param button The button
     */
    private void setImage(Character safeContent, Button button){
        ImageView x;
        switch (safeContent){
            case 'X':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillarX.png")));
                button.setGraphic(x);
                break;
            case '0':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar0.png")));
                button.setGraphic(x);
                break;
            case '1':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar1.png")));
                button.setGraphic(x);
                break;
            case '2':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar2.png")));
                button.setGraphic(x);
                break;
            case '3':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar3.png")));
                button.setGraphic(x);
                break;
            case '4':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar4.png")));
                button.setGraphic(x);
                break;
            case 'L':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/laser.png")));
                button.setGraphic(x);
                break;
            case '*':
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/beam.png")));
                button.setGraphic(x);
                break;
            default:
                x = new ImageView(new Image(getClass().getResourceAsStream("resources/white.png")));
                button.setGraphic(x);


        }
        setButtonBackground(button, "white.png");
    }
    /**
     * Bottombtn function is used to created the buttons that are displayed in
     * the bottom which include Check, Hint, Solve, Restart and Load respectiv-
     * ely.
     * @return HBox
     */
    private HBox bottombtns(){
        //BOTTOM of the borderPane             //BOTTOM Buttons
        Button checkbtn = new Button("Check");

        Button hintbtn = new Button("Hint");
        Button solvebtn = new Button("Solve");
        Button restartbtn = new Button("Restart");
        Button loadbtn = new Button("Load");
        //File Chooser is the loser here

        final FileChooser fileChooser = new FileChooser();

        loadbtn.setOnAction(event1 -> {
            System.out.println("Load Button Clicked!");
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);
        });


        restartbtn.setOnAction(event -> {
            setImage(model.getGrid()[notVerifiedRow][getNotVerifiedCol],referenceGrid[notVerifiedRow][getNotVerifiedCol]);
            model.restart();
            message.setText("Safe is reset");
        });

        checkbtn.setOnAction(event -> {
            String[] token = model.verify().split("\\s+");
            if(token.length == 1){
                message.setText("Safe is fully verified!");
            }
            else{
                notVerifiedRow = Integer.parseInt(token[0]);
                getNotVerifiedCol = Integer.parseInt(token[1]);
                message.setText("Error verifying at: (" + notVerifiedRow + ", " + getNotVerifiedCol + ")");
                if(model.getGrid()[notVerifiedRow][getNotVerifiedCol] == 'L'
                   || model.getGrid()[notVerifiedRow][getNotVerifiedCol] == '0'
                   || model.getGrid()[notVerifiedRow][getNotVerifiedCol] == '1'
                        || model.getGrid()[notVerifiedRow][getNotVerifiedCol] == '2' || model.getGrid()[notVerifiedRow][getNotVerifiedCol] == '3'
                        || model.getGrid()[notVerifiedRow][getNotVerifiedCol] == '4' || model.getGrid()[notVerifiedRow][getNotVerifiedCol] == 'X'){
                    setButtonBackground(referenceGrid[notVerifiedRow][getNotVerifiedCol],"red.png");
                }
                else{
                    ImageView x = new ImageView(new Image(getClass().getResourceAsStream("resources/red.png")));
                    referenceGrid[notVerifiedRow][getNotVerifiedCol].setGraphic(x);
                }
            }
        });

        solvebtn.setOnAction(event -> {
            try{
                model.backtrackerSolver();
                for(char[] i: model.getSolution()){
                    System.out.println(i);
                }
            }catch(FileNotFoundException exc){
                exc.getMessage();
            }
        });

        hintbtn.setOnAction(event -> {
            try{
                model.generateHint();
            }catch(FileNotFoundException exc){
                exc.getMessage();
            }
            for (int row = 0; row < model.getrDIM(); row++) {
                for (int col = 0; col < model.getcDIM(); col++) {
                    char letter = model.getHint()[row][col];
                    ImageView x;
                    switch (letter) {
                        case 'L':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/laser.png")));
                            setButtonBackground(referenceGrid[row][col], "yellow.png");
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '*':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/beam.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '0':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar0.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '1':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar1.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '2':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar2.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '3':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar3.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '4':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar4.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case 'X':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillarX.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '.':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/white.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                    }
                }
            }
            message.setText("Hint");
        });

        //BOTTOM Buttons are set here
        HBox bottombtns = new HBox();
        bottombtns.setAlignment(Pos.CENTER);       //BOTTOM buttons added here
        bottombtns.getChildren().addAll(checkbtn,hintbtn,solvebtn,restartbtn,loadbtn);

        bottombtns.setAlignment(Pos.CENTER);

        return bottombtns;
    }

    private void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Load file..");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    @Override
    public void update(Observable o, Object arg) {
        //  System.out.println(arg.toString());
        for (int row = 0; row < model.getrDIM(); row++) {
            for (int col = 0; col < model.getcDIM(); col++) {
                char letter = model.getGrid()[row][col];
                ImageView x;
                switch (letter) {
                    case 'L':
                        x = new ImageView(new Image(getClass().getResourceAsStream("resources/laser.png")));
                        setButtonBackground(referenceGrid[row][col], "yellow.png");
                        referenceGrid[row][col].setGraphic(x);
                        break;
                    case '*':
                        x = new ImageView(new Image(getClass().getResourceAsStream("resources/beam.png")));
                        referenceGrid[row][col].setGraphic(x);
                        break;
                    case '.':
                        x = new ImageView(new Image(getClass().getResourceAsStream("resources/white.png")));
                        referenceGrid[row][col].setGraphic(x);
                        break;
                }
            }
        }

        if(!model.solutionStatus()) {
            for (int row = 0; row < model.getrDIM(); row++) {
                for (int col = 0; col < model.getcDIM(); col++) {
                    char letter = model.getSolution()[row][col];
                    ImageView x;
                    switch (letter) {
                        case 'L':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/laser.png")));
                            setButtonBackground(referenceGrid[row][col], "yellow.png");
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '*':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/beam.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '0':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar0.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '1':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar1.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '2':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar2.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '3':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar3.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '4':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillar4.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case 'X':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/pillarX.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                        case '.':
                            x = new ImageView(new Image(getClass().getResourceAsStream("resources/white.png")));
                            referenceGrid[row][col].setGraphic(x);
                            break;
                    }
                }
            }
            message.setText("Safe is solved");
        }
        else{
            message.setText("Safe does not have a solution!");
        }



        for(char[] d: model.getGrid()){
            System.out.println(Arrays.toString(d));
        }

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
