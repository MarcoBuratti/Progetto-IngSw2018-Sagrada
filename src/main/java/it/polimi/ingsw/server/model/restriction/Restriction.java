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
    Boolean restrictionCheck(Die die);

}