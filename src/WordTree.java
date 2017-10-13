import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**Tree representation of dictionary. Object holds an array with a size of 26. Each position represents an alphabetical
 * character relative to there position in the alphabet. Each character of a word is one layer deeper in the depth of
 * the tree. */
public class WordTree
{
    private boolean isWord = false;
    private WordTree[] children = new WordTree[27];

    /**Initializes a new word tree based on a file path to a txt file of dictionary words.
     * @throws FileNotFoundException */
    WordTree(String dictionaryPath) {
        FileInputStream dictionaryFile;

        try { //open the wordTree file
            dictionaryFile = new FileInputStream(dictionaryPath);
            initializeDictionary(dictionaryFile);
        } catch(FileNotFoundException exc) {
            System.out.println("File Not Found");
        }
    }

    /**Initializes a new Word Tree Object*/
    WordTree(){}

    /**Takes a FileInputStream and initializes all the wordTree objects below the tree or within this tree's
     * Array.
     * @param dictionaryFile FileInputStream of the dictionary to create the tree from*/
    private void initializeDictionary(FileInputStream dictionaryFile)
    {
        Scanner scan = new Scanner(dictionaryFile);
        String currentWord;
        while(scan.hasNext()){
            currentWord = scan.nextLine().toLowerCase();
            createLeaves(currentWord);
            WordTree tempLeave = getLeave(currentWord);
            tempLeave.setToWord(true);
        }
        scan.close();
    }

    /**Sets this WordTree objects isWord value to true.
     * @param value Boolean to set isWord*/
    private void setToWord(boolean value)
    {
        isWord = value;
    }

    /**String is split into two separate partitions the head (first character) and the tail (rest of String).
     * Method instantiates new WordTree Objects in this Tree's Array based on the character value of the head and
     * recursively calls itself within that object given the tail until the tail is empty
     * @param currentWord String to recursively call upon until empty*/
    private void createLeaves(String currentWord)
    {
        if (currentWord.isEmpty()) {return;}

        int index = castToIndex(currentWord.charAt(0));
        WordTree child;

        if(children[index] == null)
        {children[index] = new WordTree();}

        child = children[index];
        child.createLeaves(currentWord.substring(1));
    }

    /**given a character value, return the WordTree Object corresponding to that value in this WordTree's array
     * @param letter Character value to return object
     * @throws NullPointerException*/
    private WordTree getObject(char letter)throws NullPointerException{
        try
        {
           int index = castToIndex(letter);
           return children[index];
        }catch (NullPointerException e){
            System.err.print(e.getStackTrace());
            System.out.println("object does not exist in Tree");
        }

        return null;
    }

    /**recursively calls itself down the tree until the passed word is empty and then returns the Leave or object
     * of the tree
     * @param word STring to recursively call until empty*/
    private WordTree getLeave(String word){
        if(word.isEmpty()){ return this; }
        return getObject(word.charAt(0)).getLeave(word.substring(1));
    }

    /**Given a character value return the corresponding integer value in ascii - 97.
     * @param letter Character value to cast to index*/
    private int castToIndex(char letter){return ((int)letter) - 97;}

    /**check whether given String may still be a valid word within the dictionary. Method Recursively calls itself down
     * the tree until it is empty. If before the word is empty a leave is found returns false.
     * @param word  String to recursively call until empty*/
    boolean isValid(String word){
        if(word.isEmpty()) return true;

        WordTree child = getObject(word.charAt(0));

        if(child != null) return child.isValid(word.substring(1));

        return false;
    }
    /**check whether given String is a valid word within the dictionary. Method Recursively calls itself down the tree
     * until it is empty. If before the word is empty a leave is found returns false.
     * @param word  String to recursively call until empty*/
    boolean containsWord(String word)
    {
        if(word.isEmpty()) return isWord;

        WordTree child = getObject(word.charAt(0));

        if(child != null) return child.containsWord(word.substring(1));

        return false;
    }




}
