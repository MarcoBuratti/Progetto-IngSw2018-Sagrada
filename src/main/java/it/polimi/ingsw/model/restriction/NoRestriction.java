package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.Die;

public class NoRestriction implements Restriction {
    @Override
    public Boolean restrictionCheck(Die die) {
        return true;
    }

    @Override
    public String toString() {
        return "There are no restrictions on this cell";
    }

    @Override
    public boolean equals (Object myObject) {
        return super.equals(myObject);
    }
}
