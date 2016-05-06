package gui;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import model.*;

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
        //border.setBackground(javafx.scene.paint.Color.LIGHTGRAY);
        //border.setStyle(("-fx-background-color:blue);
        //LEFT and RIGHT spaces in the BorderPane
        //Sets space in the left and the right
        border.setPadding(new Insets(0,70,0,70));

        int c = model.getcDIM();
        int r = model.getrDIM();

        border.setMinHeight(Math.max(c*30, r*30));
        border.setMinWidth(Math.max(c*30, r*30));
        //TOP of the borderPane : Message area indicating status of safe
        Label label1 = new Label();
        label1.setText("Message area indicating status of safe");
        // Message types:
        // The name of the safe file when initially loaded or restarted.
        // The result of attempting to add or remove a laser from the safe.
        // The status of the safe when checked for correctness
        // The result of requesting a hint for the next laser to place.
        // The result of attempting to fully solve the laser placements.

        label1.setAlignment(Pos.CENTER);
        //label1.setPadding(new Insets(20));
        border.setTop(label1);                  //TOP Label Message: Safe Status

        //CENTER of the borderPane
        border.setCenter(centerButtonPane());  //CENTER GridButtons

        //BOTTOM of the borderPane             //BOTTOM Buttons
        Button checkbtn = new Button("Check");
        Button hintbtn = new Button("Hint");
        Button solvebtn = new Button("Solve");
        Button restartbtn = new Button("Restart");
        Button loadbtn = new Button("Load");

        //BOTTOM Buttons are set here
        HBox bottombtns = new HBox();
        bottombtns.setAlignment(Pos.CENTER);       //BOTTOM buttons added here
        bottombtns.getChildren().addAll(checkbtn,hintbtn,solvebtn,restartbtn,loadbtn);
        border.setBottom(bottombtns);

        primaryStage.setTitle("Lasers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * CenterButton pane for the safe
     * @return
     */
    private GridPane centerButtonPane(){
        GridPane grid = new GridPane();
        double hgap = 15;
        double vgap = 3;
        grid.setHgap(hgap);  //gap between buttons
        grid.setVgap(vgap);

        int row;
        int col;
        int rDIM = model.getrDIM();             //getter of rows: rDIM model
        int cDIM = model.getcDIM();             //getter of cols: cDIM model
        for(row = 1; row<=rDIM; row++){
            for (col = 1; col<=cDIM; col++){
                Button btn = new Button();
                btn.setMinSize(30,30);
                btn.setStyle("-fx-background-color : white");
                grid.add(btn,col, row);

            }
        }

        grid.setGridLinesVisible(false);
        return grid;
}

    @Override
    public void update(Observable o, Object arg) {
       // System.out.println(arg.toString());
        // TODO
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
