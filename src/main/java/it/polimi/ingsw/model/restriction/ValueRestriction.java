package it.polimi.ingsw.model.restriction;


import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.exception.NotValidValueException;

public class ValueRestriction implements Restriction {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;

    public ValueRestriction(int number) throws NotValidValueException {
        try{
            if(number>0 && number<=NUMBER_OF_SIDES)
                this.number=number;
            else
                throw new NotValidValueException();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public int getRestriction() {
        return number;
    }

    @Override
    public Boolean restrictionCheck (Die die) {
        return (die.getNumber() == this.number);
    }

    @Override
    public String toString() {
        return "Value Restriction: " + number;
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
