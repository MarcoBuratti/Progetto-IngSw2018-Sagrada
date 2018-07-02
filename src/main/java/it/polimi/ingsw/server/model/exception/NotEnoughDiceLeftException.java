package it.polimi.ingsw.server.model.exception;

public class NotEnoughDiceLeftException extends Exception {

    /**
     * Creates an exception that specifies that there are not enough dice left to get from the dice bag.
     */
    public NotEnoughDiceLeftException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "There are not enough dice left in the bag!";
    }
}
