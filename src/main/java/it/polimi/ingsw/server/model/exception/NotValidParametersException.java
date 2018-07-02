package it.polimi.ingsw.server.model.exception;

public class NotValidParametersException extends Exception {

    /**
     * Creates an exception that specifies that the selected parameters are not allowed.
     */
    public NotValidParametersException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "These parameters (row and column) are incorrect";
    }
}