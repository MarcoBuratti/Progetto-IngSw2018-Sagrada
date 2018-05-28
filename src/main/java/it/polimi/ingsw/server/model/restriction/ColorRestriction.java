package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;

public class ColorRestriction implements Restriction {
    private Color color;

    /**
     * Creates a ColorRestriction Object representing a cell's color restriction.
     * @param color the color the die placed on the cell must have.
     */
    public ColorRestriction (Color color){
        this.color = color;
    }

    /**
     * Returns a Color object specifying the color of the restriction.
     * @return the color the die placed on the cell must have.
     */
    public Color getRestriction() {
        return color;
    }

    @Override
    /**
     * Returns a Boolean object which specifies if the die can be set on the cell having that
     * restriction (the die must have the same color as the restriction).
     */
    public Boolean restrictionCheck (Die die) {
        return (die.getColor().equals(this.color));
    }

    @Override
    /**
     * Returns a string which specifies the restriction's kind and color.
     */
    public String toString() {
        return "Restriction: " + color;
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
