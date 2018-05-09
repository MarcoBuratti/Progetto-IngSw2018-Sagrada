package it.polimi.ingsw.model.restriction;


import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Die;

public class ColourRestriction implements Restriction {
    private Colour colour;

    public ColourRestriction (Colour colour){
        this.colour = colour;
    }

    public Colour getRestriction() {
        return colour;
    }

    @Override
    public Boolean restrictionCheck (Die die) {
        return (die.getColour().equals(this.colour));
    }

    @Override
    public String toString() {
        return "Colour: " + colour;
    }

    @Override
    public boolean equals (Object myObject) {
        if (myObject != null) {
            if (this.getClass() == myObject.getClass()) {
                ColourRestriction colourRestriction = (ColourRestriction) myObject;
                return (this.colour.equals(colourRestriction.colour));
            } else return false;
        } else return false;
    }
}
