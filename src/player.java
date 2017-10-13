import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**Representation of all player moves and provides a Vbox With labels of past player moves.*/
public class player
{
    private ArrayList<String> Correct_Words = new ArrayList<>();
    private VBox playerMoves = new VBox();
    private Label totalScore = new Label();
    private int score = 0;


    /**Initializes a new player with initialScore set to 0*/
    public player(){
        playerMoves.getChildren().add(totalScore);
        totalScore.setText("Score: " + score);
        totalScore.setFont(Font.font("Cabin Bold",32));
    }
    /**Returns a Vbox of Labels containing this players past moves*/
    VBox getPlayerMoves(){return playerMoves;}

    /**Updates the Vbox with a not in Dictionary Label*/
    void notInDictionary(String input){
        UpdateVBox(input,false,"not a word");
    }

    /**Updates the Vbox with a not in invalid word Label*/
    void invalidWord(String input)
    {
        UpdateVBox(input,false,"invalid selection");
    }

    /**Updates the Vbox with a Word already selected Label*/
    void wordSelected(String input){
        UpdateVBox(input,false,"Already Selected");
    }

    /**Adds a correctly chosen word to the player moves and updates the player score*/
    void addWord(String word){
        int points = word.length() - 2;
        if(word.length() < 2) points = 0;
        UpdateVBox(word,true,(points + " points"));
        //playerMoves.getChildren().add(new Label(word + " " + points + " points"));
        Correct_Words.add(word);
        score += points;
        totalScore.setText("Score: " + score);
    }

    /**Adds a new Label to the Vbox setting the font and size of the label.
     * @param Word Word selected by player
     * @param Reason String representing if Player move is either false for several reasons or a correct move
     * @param valid Boolean representing if player move was valid*/
    private void UpdateVBox(String Word,Boolean valid, String Reason){
        Label output = new Label(Word + " " + Reason);
        output.setFont(Font.font("Counter-Strike",18));
        if(valid){
            output.setTextFill(Color.SEAGREEN);
        }else{
            output.setTextFill(Color.RED);
        }
        playerMoves.getChildren().add(output);
    }

    /**Checks if player already chose a word.
     * @param word String of word to check*/
    boolean playerContainsWord(String word) {
        for(String e: Correct_Words) if(e.equals(word)) return true;
        return false;
    }
}