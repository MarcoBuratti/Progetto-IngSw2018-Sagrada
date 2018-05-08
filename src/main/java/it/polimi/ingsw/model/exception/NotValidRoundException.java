package it.polimi.ingsw.model.exception;

public class NotValidRoundException extends Exception {
    public NotValidRoundException(){
        super();
    }

    @Override
    public String toString() {
        return "Round number is incorrect!";
    }
}