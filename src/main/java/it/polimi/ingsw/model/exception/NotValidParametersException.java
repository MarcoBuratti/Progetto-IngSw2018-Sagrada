package it.polimi.ingsw.model.exception;

public class NotValidParametersException extends Exception {
    public NotValidParametersException(){
        super();
    }

    @Override
    public String toString() {
        return "These parameters (row and column) are incorrect";
    }
}