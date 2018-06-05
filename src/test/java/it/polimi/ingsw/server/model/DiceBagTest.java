package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiceBagTest {

    public int colorCounter (ArrayList<Die> diceSet, Color C){
        int counter = 0;
        for(Die d: diceSet)
            if(d.getColor().equals(C))
                counter++;
        return counter;
    }
    @Test
    public void enoughDiceForEachColor(){
        DiceBag bag = new DiceBag();
        assertEquals(90, bag.getDiceSet().size());
        assertEquals(colorCounter(bag.getDiceSet(), Color.GREEN), 18);
        assertEquals(colorCounter(bag.getDiceSet(), Color.YELLOW), 18);
        assertEquals(colorCounter(bag.getDiceSet(), Color.BLUE), 18);
        assertEquals(colorCounter(bag.getDiceSet(), Color.RED), 18);
        assertEquals(colorCounter(bag.getDiceSet(), Color.VIOLET), 18);
    }

    @Test
    public void goodExtraction() throws NotEnoughDiceLeftException {
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
        Assertions.assertThrows(NotEnoughDiceLeftException.class,()->bag.extractSet(83));
        diceSet = bag.extractSet(0);
        diceSet = bag.extractSet(82);
        assertEquals(0, bag.getDiceSet().size());
    }

}