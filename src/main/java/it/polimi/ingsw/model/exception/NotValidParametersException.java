package it.polimi.ingsw.model.exception;

public class NotValidParameters extends Exception {
    public NotValidParameters(){
        super();
    }

    @Override
    public String toString() {
        return "There parameters (row and column) are not right!";
    }
}