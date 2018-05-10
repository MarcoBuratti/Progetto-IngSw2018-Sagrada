package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictionTest {
    @Test
    public void colourTest() {
        ColorRestriction c1 = new ColorRestriction(Color.BLUE);
        assertEquals(Color.BLUE, c1.getRestriction());
        Die d1 = new Die(Color.BLUE);
        assertTrue(c1.restrictionCheck(d1));
        Die d2 = new Die(Color.YELLOW);
        assertFalse(c1.restrictionCheck(d2));
        Restriction c2 = new ColorRestriction(Color.YELLOW);
        assertTrue(c2.restrictionCheck(d2));
    }

    @Test
    public void numberTest() throws NotValidNumberException {
        NumberRestriction n1 = new NumberRestriction(2);
        Restriction n2 = new NumberRestriction(6);
        assertEquals(2, n1.getRestriction());
        Die d1 = new Die(Color.GREEN);
        d1.setNumber(2);
        assertTrue(n1.restrictionCheck(d1));
        d1.setNumber(3);
        assertFalse(n1.restrictionCheck(d1));
        assertFalse(n2.restrictionCheck(d1));
    }

    @Test
    public void genericTest() throws NotValidNumberException {
        Die d1 = new Die(Color.BLUE);
        d1.setNumber(1);
        Restriction r1 = new ColorRestriction(Color.BLUE);
        assertTrue(r1.restrictionCheck(d1));
        r1 = new NumberRestriction(2);
        assertFalse(r1.restrictionCheck(d1));
        r1 = new NoRestriction();
        assertTrue(r1.restrictionCheck(d1));
    }


}