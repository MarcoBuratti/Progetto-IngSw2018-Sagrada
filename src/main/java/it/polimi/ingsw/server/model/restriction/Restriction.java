package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Die;

public interface Restriction {

    /**
     * Returns a Boolean object specifying whether it's possible or not to set the Die object passed as die argument
     * on the cell having this restriction.
     *
     * @param die the die the user wants to set on the cell having this restriction
     * @return true if the die complies with the restriction, false otherwise
     */
    boolean restrictionCheck(Die die);

    /**
     * Specifies the restriction's type.
     * @return an instance of the RestrictionType Enum, specifying the restriction's type
     */
    RestrictionType getType();

    /**
     * Compares two restrictions.
     * @param myRestriction the Restriction Object the user wants to compare to the one this method is called from
     * @return  boolean specifying whether the Restriction Object represents the same restriction as myRestriction argument
     */
    boolean restrictionEquals (Restriction myRestriction);
}