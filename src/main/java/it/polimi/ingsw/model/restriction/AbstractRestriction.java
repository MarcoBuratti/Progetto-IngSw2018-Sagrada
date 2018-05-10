package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.exception.NotValidValueException;

public abstract class AbstractRestriction {

    public abstract Restriction getRestriction(RestrictionEnum restrictionEnum) throws NotValidValueException;
}
