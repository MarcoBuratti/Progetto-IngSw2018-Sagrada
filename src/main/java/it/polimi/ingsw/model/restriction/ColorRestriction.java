package it.polimi.ingsw.model.restriction;


import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;

public class ColorRestriction extends Restriction {
    private Color color;

    public ColorRestriction(Color color){
        this.color = color;
    }

    public Color getRestriction() {
        return color;
    }

    @Override
    public Boolean restrictionCheck (Die die) {
        return (die.getColor().equals(this.color));
    }

    @Override
    public String toString() {
        return "Color: " + color;
    }

    @Override
    public boolean equals (Object myObject) {
        if (myObject != null) {
            if (this.getClass() == myObject.getClass()) {
                ColorRestriction colorRestriction = (ColorRestriction) myObject;
                return (this.color.equals(colorRestriction.color));
            } else return false;
        } else return false;
    }
}
