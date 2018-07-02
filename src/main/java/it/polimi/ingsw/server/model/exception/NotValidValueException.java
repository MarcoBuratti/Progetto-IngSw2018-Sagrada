package it.polimi.ingsw.server.model.exception;

public class NotValidValueException extends Exception {

    /**
     * Creates an exception that specifies that the chosen value is not valid (for example trying to set 0 on a die as its value)
     */
    public NotValidValueException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Please insert a number between 1 and 6";
    }
}
