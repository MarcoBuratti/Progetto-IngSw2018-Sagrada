package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictionTest {
    @Test
    public void colourTest() {
        ColourRestriction c1 = new ColourRestriction(Colour.BLUE);
        assertEquals(Colour.BLUE, c1.getRestriction());
        Die d1 = new Die(Colour.BLUE);
        assertTrue(c1.restrictionCheck(d1));
        Die d2 = new Die(Colour.YELLOW);
        assertFalse(c1.restrictionCheck(d2));
        Restriction c2 = new ColourRestriction(Colour.YELLOW);
        assertTrue(c2.restrictionCheck(d2));
    }

    @Test
    public void numberTest() throws NotValidNumberException {
        NumberRestriction n1 = new NumberRestriction(2);
        Restriction n2 = new NumberRestriction(6);
        assertEquals(2, n1.getRestriction());
        Die d1 = new Die(Colour.GREEN);
        d1.setNumber(2);
        assertTrue(n1.restrictionCheck(d1));
        d1.setNumber(3);
        assertFalse(n1.restrictionCheck(d1));
        assertFalse(n2.restrictionCheck(d1));
    }

    @Test
    public void genericTest() throws NotValidNumberException {
        Die d1 = new Die(Colour.BLUE);
        d1.setNumber(1);
        Restriction r1 = new ColourRestriction(Colour.BLUE);
        assertTrue(r1.restrictionCheck(d1));
        r1 = new NumberRestriction(2);
        assertFalse(r1.restrictionCheck(d1));
        r1 = new NoRestriction();
        assertTrue(r1.restrictionCheck(d1));
    }


}