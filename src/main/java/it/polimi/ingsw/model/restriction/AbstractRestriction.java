package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.exception.NotValidNumberException;

public abstract class AbstractRestriction {

    public abstract Restriction getRestriction(RestrictionEnum restrictionEnum) throws NotValidNumberException;
}
