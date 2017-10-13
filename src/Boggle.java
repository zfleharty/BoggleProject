
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Boggle extends Application
{
    //Game Variables
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private static GameController controls;

    //Global Components

    //Timer
    private Integer startTime = 120;
    private Integer timeSeconds = startTime;
    private String time;
    private Timeline timeline = new Timeline();
    private Label timerLabel = new Label();

    private VBox PlayerMoves;
    private Canvas Board;
    private GraphicsContext boardDisplay;
    private Button Start;
    private BorderPane rootPane;

    //EventHandlers

    /**EventHnadler for when a the board is clicked on. Get's the correct x and y value of the cube selected and passes
     * the data to the controller. Calls the controller to redraw the board. */
    private EventHandler<MouseEvent> Board_Click = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            int x = (int)(event.getX() / (Board.getWidth() / COLS));
            int y = (int)(event.getY() / (Board.getHeight() / ROWS));
            controls.addCube(x,y);
            controls.drawConnects(boardDisplay);
        }
    };
    /**EventHandler for when the mouse is dragged over a cube in the game board. Get's the correct x and y value and
     * set's an effect to the Cube. Calls to controller to redraw the board*/
    private EventHandler<MouseEvent> Mouse_Drag = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            int x = (int)(event.getX() / (Board.getWidth() / COLS));
            int y = (int)(event.getY() / (Board.getHeight() / ROWS));
            controls.setEffect(x,y);
            controls.getBoard(boardDisplay);
        }
    };


    /**EventHandler for when the start button is clicked. Set's the board to visible and set's it's eventHandler's to
     * on. StartButton is set to off. */
    private EventHandler<MouseEvent> Start_Action = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            Start.setDisable(true);
            //initialize board and add actionListeners
            controls.getBoard(boardDisplay);
            Board.addEventHandler(MouseEvent.MOUSE_ENTERED,Mouse_Drag);
            Board.addEventHandler(MouseEvent.MOUSE_MOVED,Mouse_Drag);
            Board.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,event1 -> {
                controls.setEffectsOff();
                controls.getBoard(boardDisplay);
            });
            Board.setOnMouseClicked(Board_Click);

            timeline.playFromStart();
        }
    };

    /**EventHandler for when the restart button is clicked. Creates a new gameController and set's a new visible board.
     * resets the time and all player moves.*/
    private EventHandler<MouseEvent> Restart_Game = new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            controls = new GameController(COLS, ROWS, "Resources/OpenDictionary.txt");
            controls.getBoard(boardDisplay);
            timeSeconds = startTime;
            timeline.playFromStart();
            PlayerMoves = controls.getPlayerMoves();
            PlayerMoves.setPrefSize(200,400);

            rootPane.setLeft(PlayerMoves);
            BorderPane.setAlignment(PlayerMoves, Pos.CENTER_LEFT);

            Board.setOnMouseClicked(Board_Click);
        }
    };
    /**EventHandler to countdown the timer*/
    private EventHandler<ActionEvent> CountDown = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event)
        {
            timeSeconds--;
            setTime();
            timerLabel.setText("Time " + time);
            if(timeSeconds <= 0) {
                timeline.stop();
                Board.setOnMouseClicked(null);
            }
        }
    };

    /**Starts the game with a new Game controller and launches the javafx application*/
    public static void main(String[] args)
    {
        controls = new GameController(COLS, ROWS, "Resources/OpenDictionary.txt");
        launch(args);
    }

    /**Sets Global variable time to the it's correct text based on the time remaining in the gamee*/
    private void setTime(){
        Integer minutes = timeSeconds / 60;
        Integer seconds = timeSeconds % 60;
        if(seconds < 10){
            time = minutes.toString() + ":" + "0" + seconds;
        }else{
            time = minutes.toString() + ":" + seconds.toString();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Boggle");

        ///////////////Declare all components//////////////
        rootPane = new BorderPane();
        Board = new Canvas(350,350);
        boardDisplay = Board.getGraphicsContext2D();
        PlayerMoves = controls.getPlayerMoves();
        VBox PlayableButtons = new VBox(); //Usable buttons to control game

        rootPane.setBackground(new Background(new BackgroundFill(Color.SILVER,null,null)));

        //Buttons
        Start = new Button("Start");
        Button SubmitWord = new Button("Submit Word");
        Button Restart = new Button("Restart");
        PlayableButtons.getChildren().addAll(Start,SubmitWord,Restart);


        /////////Set Component Aspects and position//////////////
        PlayerMoves.setPrefSize(200,400);
        timerLabel.setText("Time 2:00");
        timerLabel.setFont(Font.font("Cabin Bold",38));
        //for(String e: Font.getFontNames()) System.out.println(e);


        /*place all Components in rootPane(borderPane)*/
        BorderPane.setAlignment(timerLabel, Pos.TOP_CENTER);
        rootPane.setTop(timerLabel);

        rootPane.setCenter(Board);
        BorderPane.setAlignment(Board,Pos.TOP_CENTER);

        rootPane.setLeft(PlayerMoves);
        BorderPane.setAlignment(PlayerMoves,Pos.CENTER_LEFT);

        rootPane.setRight(PlayableButtons);
        BorderPane.setAlignment(PlayableButtons,Pos.CENTER_RIGHT);



        /*set all actionListeners and and timer event*/

        //Set Button Actions
        Start.setOnMouseClicked(Start_Action);
        SubmitWord.setOnMouseClicked(event -> {
            controls.submitWord();
            controls.drawConnects(boardDisplay);
        });
        Restart.setOnMouseClicked(Restart_Game);

        //Set Timer Actions
        setTime();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), CountDown));





        /////////Find way to set Scene background

        Scene scene = new Scene(rootPane,750,550);
        primaryStage.setScene(scene);

        primaryStage.show();
    }




}
