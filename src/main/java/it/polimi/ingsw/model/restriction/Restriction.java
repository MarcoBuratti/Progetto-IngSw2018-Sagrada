package it.polimi.ingsw.model;

public abstract class Restriction {

    public abstract Boolean restrictionCheck (Die die);

    @Override
    public String toString(){
        return "Restriction";
    }
}