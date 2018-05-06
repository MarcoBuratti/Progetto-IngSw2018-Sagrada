package it.polimi.ingsw.model.restriction;

import it.polimi.ingsw.model.Die;

public abstract class Restriction {

    public abstract Boolean restrictionCheck (Die die);

    @Override
    public String toString(){
        return "Restriction";
    }
}