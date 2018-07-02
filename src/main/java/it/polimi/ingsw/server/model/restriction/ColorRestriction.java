package it.polimi.ingsw.server.model.restriction;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;

public class ColorRestriction implements Restriction {
    private Color color;
    private RestrictionType type;

    /**
     * Creates a ColorRestriction Object representing a cell's color restriction.
     *
     * @param color the color the die placed on the cell must have.
     */
    ColorRestriction(Color color) {
        this.color = color;
        this.type = RestrictionType.COLOR;
    }

    /**
     * Returns a Color object specifying the color of the restriction.
     *
     * @return the color the die placed on the cell must have.
     */
    public Color getRestriction() {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RestrictionType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrictionEquals(Restriction myRestriction) {
        if (myRestriction != null) {
            if (this.getClass() == myRestriction.getClass()) {
                ColorRestriction colorRestriction = (ColorRestriction) myRestriction;
                return (this.color.equals(colorRestriction.color));
            } else return false;
        } else return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrictionCheck(Die die) {
        return (die.getColor().equals(this.color));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return color.toString();
    }


}
