package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.Restriction;
import it.polimi.ingsw.server.model.restriction.ValueRestriction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    @Test
    public void cellTest() throws NotValidValueException, OccupiedCellException {
        Restriction r1 = new ValueRestriction(5);
        Cell c1 = new Cell(r1);
        assertEquals(r1, c1.getRestriction());
        assertFalse(c1.getUsedCell());
        assertEquals(null, c1.getDie());
        Die d1 = new Die(Color.VIOLET);
        Die d2 = new Die(Color.YELLOW);
        d1.setNumber(6);
        assertFalse(c1.allowedMove(d1));
        d1.setNumber(5);
        assertTrue(c1.allowedMove(d1));
        c1.setDie(d1);
        assertThrows(OccupiedCellException.class, ()->c1.setDie(d2));
        assertEquals(d1, c1.getDie());
        assertTrue(c1.getUsedCell());
        Cell c2 = c1.copyConstructor();
        assertTrue(c1.cellEquals(c2));
        d1 = c1.removeDie();
        assertEquals(null, c1.getDie());
        assertEquals(d1, c2.getDie());
        assertFalse(c1.cellEquals(c2));
    }

}