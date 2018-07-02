package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Die;

public class NoRestriction implements Restriction {
    private RestrictionType type;

    public NoRestriction() {
        this.type = RestrictionType.NO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrictionCheck(Die die) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RestrictionType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrictionEquals(Restriction myRestriction) {
        if (myRestriction != null)
            return (this.getClass() == myRestriction.getClass());
        else return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return " ";
    }


}
