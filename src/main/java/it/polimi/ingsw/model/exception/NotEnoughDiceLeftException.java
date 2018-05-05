package it.polimi.ingsw.model;

public class NotEnoughDiceLeftException extends Exception {
    public NotEnoughDiceLeftException(){
        super();
    }

    @Override
    public String toString() {
        return "There are not enough dice left in the bag!";
    }
}
