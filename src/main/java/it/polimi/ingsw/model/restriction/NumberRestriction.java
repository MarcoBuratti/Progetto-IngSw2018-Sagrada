package it.polimi.ingsw.model;


public class NumberRestriction extends Restriction {
    private static final int NUMBER_OF_SIDES = 6;
    private int number;

    public NumberRestriction (int number) throws NumberNotValidException{
        try{
            if(number>0 && number<=NUMBER_OF_SIDES)
                this.number=number;
            else
                throw new NumberNotValidException();
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
}
