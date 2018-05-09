package it.polimi.ingsw.model.exception;

public class NotEnoughFavourTokensLeft extends Exception {
    public NotEnoughFavourTokensLeft() {
        super();
    }

    @Override
    public String toString() {
        return "You don't have enough favour tokens left!";
    }
}
