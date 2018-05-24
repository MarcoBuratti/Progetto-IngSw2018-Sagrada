package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.exception.NotValidValueException;

public abstract class AbstractRestriction {

    public abstract Restriction getRestriction(RestrictionEnum restrictionEnum) throws NotValidValueException;
}
