package it.polimi.ingsw.model.restriction;


import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.exception.NotValidNumberException;

public class NumberRestriction extends Restriction {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;

    public NumberRestriction (int number) throws NotValidNumberException {
        try{
            if(number>0 && number<=NUMBER_OF_SIDES)
                this.number=number;
            else
                throw new NotValidNumberException();
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
        return "Number: " + number;
    }

    @Override
    public boolean equals (Object myObject) {
        if( myObject != null ) {
            if (this.getClass() == myObject.getClass()) {
                NumberRestriction numberRestriction = (NumberRestriction) myObject;
                return (this.number == numberRestriction.number);
            } else return false;
        }else return false;
    }
}
