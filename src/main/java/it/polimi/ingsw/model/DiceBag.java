package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;

public class DiceBag {
    private static final int NUMBER_OF_COLOURS = Colour.values().length;
    private static final int NUMBER_OF_DICE = 90;
    private static final Colour[] coloursIndex = Colour.values();

    private ArrayList<Die> diceSet = new ArrayList<Die> ();

    public DiceBag() {
        for (int i = 0; i < NUMBER_OF_COLOURS; i++)
            for (int j = 0; j < (NUMBER_OF_DICE / NUMBER_OF_COLOURS); j++) {
                this.diceSet.add(new Die(coloursIndex[i]));
            }
        Collections.shuffle(diceSet);
    }

    public ArrayList<Die> getDiceSet() {
        return diceSet;
    }

    public Die extract () {
        Die myDie = diceSet.get(0);
        diceSet.remove(0);
        return myDie;
    }

    public Die changeDie ( Die myDie ){
        Die myNewDie;
        myDie.extractAgain();
        diceSet.add(myDie);
        Collections.shuffle(diceSet);
        myNewDie = this.extract();
        return myNewDie;
    }

    public ArrayList<Die> extractSet (int quantity) throws NotEnoughDiceLeftException {
        ArrayList<Die> mySet = new ArrayList<Die>();
        try {
            if (quantity <= this.diceSet.size()) {
                Die newDie;
                for (int i = 0; i < quantity; i++) {
                    newDie = this.extract();
                    mySet.add(newDie);
                }
            }else
                throw new NotEnoughDiceLeftException();
        }catch(Exception e){
            System.out.println(e);
        }
        return mySet;
    }

    void dump(){
        System.out.println(this);
    }

    @Override
    public String toString(){
        return "This bag contains " + this.diceSet.size() + " dice \n";
    }
}