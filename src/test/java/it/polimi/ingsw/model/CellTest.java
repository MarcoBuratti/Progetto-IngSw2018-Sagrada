package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.restriction.NumberRestriction;
import it.polimi.ingsw.model.restriction.Restriction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    @Test
    public void cellTest() throws NotValidNumberException {
        Restriction r1 = new NumberRestriction(5);
        Cell c1 = new Cell(r1);
        assertEquals(r1, c1.getRestriction());
        assertFalse(c1.getUsedCell());
        assertEquals(null, c1.getDie());
        Die d1 = new Die(Colour.VIOLET);
        d1.setNumber(6);
        assertFalse(c1.allowedMove(d1));
        d1.setNumber(5);
        assertTrue(c1.allowedMove(d1));
        c1.setDie(d1);
        assertEquals(d1, c1.getDie());
        assertTrue(c1.getUsedCell());
        Cell c2 = c1.clone();
        assertTrue(c1.equals(c2));
        d1 = c1.removeDie();
        assertEquals(null, c1.getDie());
        assertEquals(d1, c2.getDie());
        assertFalse(c1.equals(c2));
    }

}