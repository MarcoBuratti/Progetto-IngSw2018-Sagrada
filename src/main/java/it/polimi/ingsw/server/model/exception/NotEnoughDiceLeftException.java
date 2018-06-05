package it.polimi.ingsw.server.model.exception;

public class NotEnoughDiceLeftException extends Exception {
    public NotEnoughDiceLeftException() {
        super();
    }

    @Override
    public String toString() {
        return "There are not enough dice left in the bag!";
    }
}
