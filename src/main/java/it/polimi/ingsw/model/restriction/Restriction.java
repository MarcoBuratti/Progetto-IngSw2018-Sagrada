package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.Die;

public interface Restriction {

    public abstract Boolean restrictionCheck(Die die);

}