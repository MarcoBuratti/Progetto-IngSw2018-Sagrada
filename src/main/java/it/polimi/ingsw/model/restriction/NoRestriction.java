package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.Die;

public class NoRestriction extends Restriction {
    @Override
    public Boolean restrictionCheck(Die die) {
        return true;
    }

    @Override
    public String toString() {
        return "There are no restrictions on this cell";
    }
}
