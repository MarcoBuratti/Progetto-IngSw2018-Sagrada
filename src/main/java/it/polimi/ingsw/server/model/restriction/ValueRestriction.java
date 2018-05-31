package it.polimi.ingsw.server.model.restriction;


import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class ValueRestriction implements Restriction {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;

    /**
     * Creates a ValueRestriction Object representing a cell's value restriction.
     * @param value the value the die placed on the cell must have.
     * @throws NotValidValueException whether the user tries to create a restriction using a value
     * that is not allowed (lesser than 1 or greater than the number of sides of the dice)
     */
    public ValueRestriction(int value) throws NotValidValueException {
        try{
            if(value>0 && value<=NUMBER_OF_SIDES)
                this.number=value;
            else
                throw new NotValidValueException();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Returns an int specifying the value of the restriction.
     * @return the value the die placed on the cell must have.
     */
    public int getRestriction() {
        return number;
    }

    @Override
    /**
     * Returns a Boolean object which specifies if the die can be set on the cell having that
     * restriction (the die must have the same value as the restriction).
     */
    public Boolean restrictionCheck (Die die) {
        return (die.getNumber() == this.number);
    }

    @Override
    /**
     * Returns a string which specifies the restriction's kind and value.
     */
    public String toString() {
        return "" + number;
    }

    @Override
    public boolean equals (Object myObject) {
        if( myObject != null ) {
            if (this.getClass() == myObject.getClass()) {
                ValueRestriction valueRestriction = (ValueRestriction) myObject;
                return (this.number == valueRestriction.number);
            } else return false;
        }else return false;
    }
}
