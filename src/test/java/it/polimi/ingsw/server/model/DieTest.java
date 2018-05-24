package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    @Test
    public void numberFromOneToSix() throws NotValidValueException {
        Die myDie = new Die(Color.GREEN);
        assertTrue(myDie.getNumber()>0 && myDie.getNumber()<7);
        myDie.extractAgain();
        assertTrue(myDie.getNumber()>0 && myDie.getNumber()<7);
        myDie.setNumber(6);
        assertEquals(6, myDie.getNumber());
        Assertions.assertThrows(NotValidValueException.class,()->myDie.setNumber(0));
        assertEquals(6, myDie.getNumber());
        myDie.setNumber(1);
        assertEquals(1, myDie.getNumber());
        myDie.extractAgain();
        assertFalse(myDie.getNumber()>6 || myDie.getNumber()<1);
        Assertions.assertThrows(NotValidValueException.class,()->myDie.setNumber(8));
    }

    @Test
    public void colorsTest(){
        Die myDie1 = new Die(Color.valueOf("GREEN"));
        assertEquals(Color.GREEN, myDie1.getColor());
        Die myDie2 = new Die(Color.values()[2]);
        assertEquals(Color.valueOf("BLUE"), myDie2.getColor());
        assertFalse(myDie1.getColor().equals(myDie2.getColor()));
        myDie2 = new Die(Color.GREEN);
        assertTrue(myDie1.getColor().equals(myDie2.getColor()));
    }
}