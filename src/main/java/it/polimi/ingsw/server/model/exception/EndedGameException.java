package it.polimi.ingsw.server.model.exception;

public class EndedGameException extends Exception {

    /**
     * Creates an exception which specifies the index of the round is incorrect because is greater than the number of rounds allowed by the game.
     */
    public EndedGameException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "The last round has already been played!";
    }
}
