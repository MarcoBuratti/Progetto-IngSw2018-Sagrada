package it.polimi.ingsw.model.exception;

public class NotValidValueException extends Exception {
    public NotValidValueException(){
        super();
    }

    @Override
    public String toString(){
        return "Please insert a number between 1 and 6";
    }
}
