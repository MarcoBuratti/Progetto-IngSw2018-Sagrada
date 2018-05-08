package it.polimi.ingsw.model.exception;

public class EndedGameException extends Exception {
    public EndedGameException(){
        super();
    }

    @Override
    public String toString() {
        return "The last round has already been played!";
    }
}
