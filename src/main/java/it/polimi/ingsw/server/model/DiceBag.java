package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;

import java.util.ArrayList;
import java.util.Collections;

public class DiceBag {
    private static final int NUMBER_OF_COLOURS = Color.values().length;
    private static final int NUMBER_OF_DICE = 90;
    private static final Color[] colorsIndex = Color.values();

    private ArrayList<Die> diceSet = new ArrayList<Die>();

    /**
     * Creates a DiceBag object representing a bag containing some dice.
     * An ArrayList contains the Die objects.
     * The bag must contain the same number of dice for each color.
     * The dice set is finally shuffled.
     */
    public DiceBag() {
        for (int i = 0; i < NUMBER_OF_COLOURS; i++)
            for (int j = 0; j < (NUMBER_OF_DICE / NUMBER_OF_COLOURS); j++) {
                this.diceSet.add(new Die(colorsIndex[i]));
            }
        Collections.shuffle(diceSet);
    }

    /**
     * Returns an ArrayList of Die objects representing the dice which are inside the bag.
     *
     * @return the list of dice inside the bag
     */
    public ArrayList<Die> getDiceSet() {
        return diceSet;
    }

    /**
     * Gives the user the possibility to extract a Die object, removing it from the dice set included
     * in the DiceBag object.
     * Even if the extracted die is always the ArrayList's head, the extraction is random because
     * the list is shuffled when the DiceBag object is created.
     *
     * @return a die that was included in the dice bag and now is no more
     */
    public Die extract() {
        Die myDie = diceSet.get(0);
        diceSet.remove(0);
        return myDie;
    }

    /**
     * Gives the user the opportunity to swap a Die Object for another one included in the DiceBag's dice set.
     * The given die's number is randomly selected again before the die is put into the bag.
     * Then the dice set is shuffled again before extracting the new die from it.
     *
     * @param myDie the die you want to put in the bag once again
     * @return a new die extracted from the dice bag
     */
    public Die changeDie(Die myDie) {
        Die myNewDie;
        myDie.extractAgain();
        diceSet.add(myDie);
        Collections.shuffle(diceSet);
        myNewDie = this.extract();
        return myNewDie;
    }

    /**
     * Returns an ArrayList of Die Objects that were included in the DiceBag's dice set.
     * The ArrayList's size is given from the user through the quantity parameter.
     * The allowed range for the quantity is between 0 (no dice extracted) and the number
     * of dice left in the bag.
     *
     * @param quantity the number of dice the user wants to extract from the bag
     * @return a dice set containing the required number of dice
     * @throws NotEnoughDiceLeftException if the user requests a quantity of dice that is no available
     */
    public ArrayList<Die> extractSet(int quantity) throws NotEnoughDiceLeftException {
        ArrayList<Die> mySet = new ArrayList<Die>();
        if (quantity <= this.diceSet.size()) {
            Die newDie;
            for (int i = 0; i < quantity; i++) {
                newDie = this.extract();
                mySet.add(newDie);
            }
        } else
            throw new NotEnoughDiceLeftException();
        return mySet;
    }


    @Override
    /**
     * Returns a string specifying the number of dice included in the bag.
     */
    public String toString() {
        return "This bag contains " + this.diceSet.size() + " dice \n";
    }
}