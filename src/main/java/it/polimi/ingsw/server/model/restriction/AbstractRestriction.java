package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.exception.NotValidValueException;

public interface AbstractRestriction {

    Restriction getRestriction(RestrictionEnum restrictionEnum) throws NotValidValueException;
}
