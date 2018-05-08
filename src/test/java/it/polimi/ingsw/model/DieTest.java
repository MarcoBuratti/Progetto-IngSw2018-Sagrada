package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    @Test
    public void numberFromOneToSix() throws NotValidNumberException {
        Die myDie = new Die(Colour.GREEN);
        assertTrue(myDie.getNumber()>0 && myDie.getNumber()<7);
        myDie.extractAgain();
        assertTrue(myDie.getNumber()>0 && myDie.getNumber()<7);
        myDie.setNumber(6);
        assertEquals(6, myDie.getNumber());
        Assertions.assertThrows(NotValidNumberException.class,()->myDie.setNumber(0));
        assertEquals(6, myDie.getNumber());
        myDie.setNumber(1);
        assertEquals(1, myDie.getNumber());
        myDie.extractAgain();
        assertFalse(myDie.getNumber()>6 || myDie.getNumber()<1);
        Assertions.assertThrows(NotValidNumberException.class,()->myDie.setNumber(8));
    }

    @Test
    public void coloursTest(){
        Die myDie1 = new Die(Colour.valueOf("GREEN"));
        assertEquals(Colour.GREEN, myDie1.getColour());
        Die myDie2 = new Die(Colour.values()[2]);
        assertEquals(Colour.valueOf("BLUE"), myDie2.getColour());
        assertFalse(myDie1.getColour().equals(myDie2.getColour()));
        myDie2 = new Die(Colour.GREEN);
        assertTrue(myDie1.getColour().equals(myDie2.getColour()));
    }
}