package it.polimi.ingsw.model;

public class ColourRestriction extends Restriction {
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
}