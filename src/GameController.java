import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**Object represents a controller to the game in which a WordTree is created to represented the dictionary and player's
 * are instantiated. Along with a gameboard to represent the board.*/
public class GameController
{
    private WordTree dictionary;
    private Tray gameBoard;
    private player player = new player();
    private ArrayList<Cube> WordSelected = new ArrayList<>(25);
    private int Row;
    private int Col;

    /**Constructor initializes a new WordTree object of words from the String path to a dictionary file. A new Tray
     * object is initialized as the gameBoard. Tray is initialized with the sizes passed.
     * @param col Number of columbs in the game board
     * @param row Number of rows in the game board
     * @param dictionaryPath String path to a dictionary text file*/
    GameController(int row, int col, String dictionaryPath){
        dictionary = new WordTree(dictionaryPath);
        gameBoard = new Tray(row,col);
        Row = row;
        Col = col;
    }

    /**Get the Vbox of the player moves from player*/
    public VBox getPlayerMoves(){return player.getPlayerMoves();}

    /**Add's the selected piece at the given x y position to the currentWord. If the move is not a valid move
     * or it can not possibly form a word then the word is cleared and playerMoves is updated with the reason for
     * a false move.
     * @param x Row of Cube
     * @param y Column of Cube*/
    public void addCube(int x, int y){

        String word = "";
        WordSelected.add(gameBoard.getCube(x,y));
        for(Cube e:WordSelected) word += e.getValue();

        if(!gameBoard.isValidWord(WordSelected)){
            player.invalidWord(word);
            WordSelected.clear();
        }else if(!dictionary.isValid(word)){
            player.notInDictionary(word);
            WordSelected.clear();
        }
    }

    /**Passes the GraphicsContext of a canvas onto the gameBoard so that it will paint the corresponding cubes onto
     * it. DrawConnects is called to ensure any connections do not get drawn over on previous canvas.
     * @param gc GraphicContext of Canvas to represent gameBoard*/
    public void getBoard(GraphicsContext gc)
    {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();
        gameBoard.getBoardView(gc,width,height);
        drawConnects(gc);
    }

    /**Submit's the current word as a player move. If the word is both a valid move on the board and exists within the
     * dictionary then the word is added to PlayerMoves as true and the points are added on accordingly otherwise the
     * word is added to PlayerMoves as false and the reason is stated in the Vbox of the player object. CurrentWord is
     * cleared*/
    public void submitWord()
    {
        String word = "";
        for(Cube e: WordSelected) word += e.getValue();

        if(!dictionary.containsWord(word)){
            player.notInDictionary(word);
        }else if(player.playerContainsWord(word)){
            player.wordSelected(word);
        }else{
            player.addWord(word);
        }

        WordSelected.clear();
    }

    /**Method draws on a canvas using a graphics context strokes between the current word being selected.
     * @param gc GraphicContext of canvas to draw on.*/
    public void drawConnects(GraphicsContext gc)
    {
        double width = gc.getCanvas().getWidth();
        double height= gc.getCanvas().getHeight();
        double CubeWidth = width / Row;
        double CubeHeight = height / Col;

        if(WordSelected.size() == 0) {
            gameBoard.getBoardView(gc,width,height);
            return;
        }else if(WordSelected.size() == 1){
            Cube cube = WordSelected.get(0);
            double x,y;
            x = (gameBoard.getXYPosition(cube)[0] * CubeWidth) + (CubeWidth / 2);
            y = (gameBoard.getXYPosition(cube)[1] * CubeHeight) + (CubeHeight / 2);
            gc.setStroke(Color.BLACK);
            gc.fillOval(x - 10, y - 10,20,20);
            return;
        }

        for(int i = 0; i < WordSelected.size() - 1;i++){
            Cube Start = WordSelected.get(i);
            Cube End = WordSelected.get(i + 1);

            double x1,x2,y1,y2;
            x1 = (gameBoard.getXYPosition(Start)[0] * CubeWidth) + (CubeWidth / 2);
            y1 = (gameBoard.getXYPosition(Start)[1] * CubeHeight) + (CubeHeight / 2);
            x2 = (gameBoard.getXYPosition(End)[0] * CubeWidth) + (CubeWidth / 2);
            y2 = (gameBoard.getXYPosition(End)[1] * CubeHeight) + (CubeHeight / 2);



            //Draw Edge connecting Cubes
            gc.setStroke(Color.LIMEGREEN);
            gc.setLineWidth(15);
            gc.strokeLine(x1,y1,x2,y2);

            //Draw Vertice Between Edge
            gc.setStroke(Color.BLACK);
            gc.fillOval(x1 - 10,y1 - 10,20,20);
            gc.fillOval(x2 - 10,y2 - 10,20,20);
        }
    }

    /**Calls the gameboard with the given x and y values to set the dropShadow effect on the corresponding Cube.
     * @param x Row of cube.
     * @param y Column of cube.*/
    public void setEffect(int x, int y) { gameBoard.setEffect(x,y);
    }

    /**Calls the game board to turn the DropShadow effect off for any Cubes which may have it turned on*/
    public void setEffectsOff() {
        gameBoard.turnOffEffect();
    }
}
