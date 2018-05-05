package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class DiceBagTest {

    public int colourCounter (ArrayList<Die> diceSet, Colour C){
        int counter = 0;
        for(Die d: diceSet)
            if(d.getColour().equals(C))
                counter++;
        return counter;
    }
    @Test
    public void enoughDiceForEachColour(){
        DiceBag bag = new DiceBag();
        assertEquals(90, bag.getDiceSet().size());
        assertEquals(colourCounter(bag.getDiceSet(), Colour.GREEN), 18);
        assertEquals(colourCounter(bag.getDiceSet(), Colour.YELLOW), 18);
        assertEquals(colourCounter(bag.getDiceSet(), Colour.BLUE), 18);
        assertEquals(colourCounter(bag.getDiceSet(), Colour.RED), 18);
        assertEquals(colourCounter(bag.getDiceSet(), Colour.VIOLET), 18);
    }

    @Test
    public void goodExtraction() throws NotEnoughDiceLeftException{
        DiceBag bag = new DiceBag();
        Die d1,d2;
        d1 = bag.extract();
        assertEquals(89, bag.getDiceSet().size());
        d2 = bag.changeDie(d1);
        System.out.println(d1.toString() + d2.toString());
        assertEquals(89, bag.getDiceSet().size());
        ArrayList<Die> diceSet = bag.extractSet(7);
        assertEquals(7, diceSet.size());
        assertEquals(82, bag.getDiceSet().size());
        diceSet = bag.extractSet(83);
        diceSet = bag.extractSet(0);
        diceSet = bag.extractSet(82);
        assertEquals(0, bag.getDiceSet().size());
    }

}