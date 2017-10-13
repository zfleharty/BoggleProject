import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**Implemenents an object to represent an instance of the boggle tray. Upon initialization the class can extend any
 * number of sized trays and automatically instantiates the necessary amount of Cube objects at random. All cubes
 * initialized will also have there adjacent pairs set within there own objects*/
class Tray
{
    private Cube[][] cubeSections;
    private int bound1;
    private int bound2;
    private Cube CurrentEffect;

    /**Constructor takes two integer values to represent the number of rows and columns the tray has. The
     * first integer represents the number of rows and the second represents the number of columns.
     * @param Rows number of rows
     * @param Cols number of cols*/
    Tray(int Rows, int Cols){
        cubeSections = new Cube[Rows][Cols];
        bound1 = Rows;
        bound2 = Cols;
        initializeCubes();
        SetAdjPairs();
    }

    /**Method sets all cubes, assuming they have been initialized, to hold the data of which objects are adjacent
     * to them. */
    private void SetAdjPairs()
    {
        for(int i = 0; i < bound1; i++){
            for(int j = 0; j < bound2; j++){
                cubeSections[i][j].setAdjCubes(getAdjCubes(i,j));
            }
        }
    }

    /**Method returns all adjacent cubes of the given position on the board and returns the Cubes in an arraylist.
     * @param i Row of Cube
     * @param j Column of Cube*/
    private ArrayList<Cube> getAdjCubes(int i, int j)
    {
        ArrayList<Cube> result = new ArrayList<>();

        if(i != 0){result.add(cubeSections[i - 1][j]);}
        if(j != 0){result.add(cubeSections[i][j - 1]);}
        if(j != 0 && i != 0){result.add(cubeSections[i - 1][j -1]);}

        if((i + 1) != bound1){result.add(cubeSections[i + 1][j]);}
        if((j + 1) != bound2){result.add(cubeSections[i][j + 1]);}
        if((i + 1) != bound1 && (j + 1) != bound2){result.add(cubeSections[i + 1][j + 1]);}

        if(i != 0 && (j + 1) != bound2){result.add(cubeSections[i - 1][j + 1]);}
        if(j != 0 && (i + 1) != bound1){result.add(cubeSections[i + 1][j - 1]);}

        return result;
    }

    /**Given a randomly generated double value (between 0 and 1) and an even partition of 26 probability distributions
     * which add up to 1, returns the integer value of the character, (corresponding to it's value in the alphabet)
     * whose value was randomly generated according to the probability distribution. For example: let x=rand(1) = .32
     * and let P[x] = the set of probability distributions. Say the summation of P[x] from 0 to 12 = .31 and the
     * summation of P[x] = from 0 to 13 = .33. Than X falls between the values of 12 and 13 but would return the
     * lesser value 12. If the loop completes but did not fall between any values (x >= 1) then 25 is returned.
     * @param distribution partition of 1 into 26 double values corresponding to probabilities of the letter
     * @param x Randomly Generated double value, should fall between 0 and 1 */
    private int FindLetter(double x, double[] distribution){
        double prev, next;
        prev = 0;
        for(int i = 0; i < 26; i++){
            next = prev + distribution[i];
            if(x >= prev && x < next) return i;
            prev = next;
        }

        return 26;
    }

    /**Function generates all needed cubes according to this Trays given size. Cubes are generated randomly based on
     * the probability of an english letter appearing in a word. All letters which occur 4 times are not generated
     * any more.*/
    private void initializeCubes()
    {
        int bound = bound1 * bound2; //number of cubes to generate
        Random rand = new Random();
        int[] frequencies = new int[26]; //Represent the Frequency of letters as they are generated
        char[] Generated_Letters = new char[bound];

        //Probabilities from https://en.wikipedia.org/wiki/Letter_frequency
        double[] Probability_Distribution = {.08167,.01492,.02782,.04253,.12702,.0228,.02015,.06094,.06966,.00153,
        .0072,.04025,.02406,.06749,.07507,.01929,.00095,.05987,.06327,.09056,.02758,.00978,.02360,.00150,.01974,.00074};

        int count = 0;
        while(count < bound){
            double Random_Variable = rand.nextDouble();
            int pick = FindLetter(Random_Variable,Probability_Distribution); //pick from probability distribution

            //Check if generated 4 times
            if(frequencies[pick] < 4){
                Generated_Letters[count] = (char)(pick + 97);
                frequencies[pick]++;
                count++;
            }
        }

        count = 0;
        for(int i = 0; i < bound1; i++){
            for(int j = 0; j < bound2; j++){
                cubeSections[i][j] = new Cube(Generated_Letters[count]);
                count++;
            }
        }
    }

    /**Given a Cube object if the cube exists within the tray, it's corresponding x = row, y = col value will
     * be returned in an array of two integers {x,y}.
     * @param cube Cube object within the tray*/
    int[] getXYPosition(Cube cube){
        int[] position = new int[2];

        for(int x = 0; x < bound1; x++){
            for(int y = 0; y < bound2;y++){
                if(cubeSections[x][y] == cube){
                    position[0] = x;
                    position[1] = y;
                    return position;
                }
            }
        }
        return position;
    }

    /**Takes two integer values x and y and returns the corresponding cube at that position Cubes[x][y].
     * @param x row of position
     * @param y Column of position
     * @return Cube of x,y position given**/
    Cube getCube(int x, int y)
    {
        return cubeSections[x][y];
    }

    /**Returns a boolean corresponding to whether the selection of cubes is a valid selection in the tray. The given
     * ArrayList of Cubes (input) from positon 0 to input.size() - 1 represent the word in order. If no Cube object
     * is repeated within the arraylist and all adjacent Cubes in the ArrayList are considered adjacent on the tray
     * then the input is considered valid and the method returns true. Otherwise the method returns false. Note: This
     * Method only checks if the letters selected are a valid selection but does not check if the word makes sense or
     * is a word, contradictory of the name of the method.
     * @param input ArrayList<Cube> representing the word selected*/
    boolean isValidWord(ArrayList<Cube> input){
        Cube currentCube;
        ArrayList<Cube> word = new ArrayList<>();
        word.addAll(input);
        while(word.size() > 1){
            currentCube = word.remove(0);
            if(word.contains(currentCube)) return false;
            if(!currentCube.adjCubes.contains(word.get(0))) return false;
        }
        return true;
    }

    /**takes a graphicContext and draws a javafx representation of the tray onto the GraphicsContext corresponsing
     * Canvas.
     * @param gc GraphicsContext corresponding to the canvas to be drawn on.
     * @param width double representing the width of the canvas.
     * @param height double representing the height of the canvas.*/
    void getBoardView(GraphicsContext gc, double width, double height){
        double cubeWidth = width / bound1;
        double cubeHeight = height / bound2;
        gc.clearRect(0,0,width,height);

        for(int i = 0; i < bound1; i++){
            for(int j = 0; j < bound2; j++){
                double x = i * cubeWidth;
                double y = j * cubeHeight;
                gc.drawImage(cubeSections[i][j].getImage(),x,y,cubeWidth,cubeHeight);
            }
        }
    }

    /**Sets the current Cube with an effect added to it's image to off*/
    void turnOffEffect(){
        if(CurrentEffect != null) CurrentEffect.setEffectOff();
        CurrentEffect = null;
    }

    /**Sets the cube at position x and y to have a drop shadow effect on it's image
     * @param x row of cube
     * @param y Column of cube**/
    void setEffect(int x, int y) {
        Cube cube = cubeSections[x][y];
        if(CurrentEffect != null && cube != CurrentEffect) CurrentEffect.setEffectOff();
        CurrentEffect = cube;
        cube.setEffectOn();
    }
}
