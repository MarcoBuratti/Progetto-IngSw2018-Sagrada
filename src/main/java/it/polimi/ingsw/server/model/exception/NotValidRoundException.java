package it.polimi.ingsw.server.model.exception;

public class NotValidRoundException extends Exception {

    /**
     * Creates an exception that specifies that the index of the selected round doesn't exist.
     */
    public NotValidRoundException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Round number is incorrect!";
    }
}