package it.polimi.ingsw.server.model.restriction;


import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class ValueRestriction implements Restriction {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;
    private RestrictionType type;

    /**
     * Creates a ValueRestriction Object representing a cell's value restriction.
     *
     * @param value the value the die placed on the cell must have.
     * @throws NotValidValueException whether the user tries to create a restriction using a value
     *                                that is not allowed (lesser than 1 or greater than the number of sides of the dice)
     */
    public ValueRestriction(int value) throws NotValidValueException {
        if (value > 0 && value <= NUMBER_OF_SIDES) {
            this.number = value;
            this.type = RestrictionType.VALUE;
        } else
            throw new NotValidValueException();
    }

    /**
     * Returns an int specifying the value of the restriction.
     *
     * @return the value the die placed on the cell must have.
     */
    public int getRestriction() {
        return number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrictionCheck(Die die) {
        return (die.getNumber() == this.number);
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
    public String toString() {
        return "" + number;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrictionEquals(Restriction restriction) {
        if (restriction != null) {
            if (this.getClass() == restriction.getClass()) {
                ValueRestriction valueRestriction = (ValueRestriction) restriction;
                return (this.number == valueRestriction.number);
            } else return false;
        } else return false;
    }

}
