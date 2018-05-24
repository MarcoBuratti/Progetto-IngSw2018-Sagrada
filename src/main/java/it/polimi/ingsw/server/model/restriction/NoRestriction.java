package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Die;

public class NoRestriction implements Restriction {

    @Override
    /**
     * Specifies whether it is possible to set a die on the cell having this restriction.
     * Always returns true as the NoRestriction Object specifies there are no restrictions on the cell.
     */
    public Boolean restrictionCheck(Die die) {
        return true;
    }

    @Override
    /**
     * Returns a string telling there are no restriction on the cell.
     */
    public String toString() {
        return "There are no restrictions on this cell";
    }

    @Override
    public boolean equals (Object myObject) {
        if ( myObject != null )
            return (this.getClass() == myObject.getClass());
        else return false;
    }
}
