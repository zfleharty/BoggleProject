import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;


/**Class provides an object to represent Letters on the board. Class features data storing
 * cubes considered adjacent to it, checking whether a given object is adjacent to it and
 * an image to represent cube in a javafx program**/
public class Cube {

    private char value;
    private ImageView cubeImage;
    ArrayList<Cube> adjCubes = new ArrayList<>();
    private DropShadow Effect = new DropShadow();


    /**Constructor initializes a new cube with a value of the given character. Constructor initializes the
     * appropriate image. Note that this constructor does not set the appropriate adjacent cubes and the method
     * getAdjCubes will return a null pointer exception.
     * @param letter Character value of the cube*/
    public Cube(char letter) {
        value = letter;
        Image image = new Image("File:Resources/Letter" + (char) ((int) letter - 32) + ".png");
        cubeImage = new ImageView(image);
        Effect.setColor(Color.BLACK);
        Effect.setRadius(2);
    }

    /**returns the character value of the cube
     * @return Char: value of cube*/
    public char getValue() {
        return value;
    }

    /**Returns a javafx image of the cube based on the character value
     * @return Image: javafx image*/
    public Image getImage() {
        SnapshotParameters params = new SnapshotParameters();
        Image returnImage = cubeImage.snapshot(params, null);
        return returnImage;
    }

    /**returns an arraylist of Cubes which have a character value that matches the given
     * character value
     * @param e Character value to match with adjacent cubes
     * @return ArrayList<Cube> of matching adjacent cubes**/
    public ArrayList<Cube> getMatchingAdjCubes(char e) {
        ArrayList<Cube> result = new ArrayList<>();
        for (Cube cube : adjCubes) {
            if (e == cube.getValue()) result.add(cube);
        }
        return result;
    }

    /**Given an arraylist of Cubes set all cubes to the value of adjCubes(Cubes considered adjacent to this cube
     * on the board.
     * @param cubes ArrayList of cubes to add to this objects adjCubes*/
    public void setAdjCubes(ArrayList<Cube> cubes) {
        adjCubes.addAll(cubes);
    }


    /**Turns off any effect set to this cubes ImageView */
    public void setEffectOff() {
        cubeImage.setEffect(null);
    }

    /**Sets a dropShadow effect to this Cubes Javafx ImageView*/
    public void setEffectOn() {
        cubeImage.setEffect(Effect);
    }

}
