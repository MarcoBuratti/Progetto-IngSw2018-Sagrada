package it.polimi.ingsw.model.exception;

public class NotValidParametersException extends Exception {
    public NotValidParametersException(){
        super();
    }

    @Override
    public String toString() {
        return "There parameters (row and column) are not right!";
    }
}