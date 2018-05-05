package it.polimi.ingsw.model.exception;

public class NumberNotValidException extends Exception {
    public NumberNotValidException(){
        super();
    }

    @Override
    public String toString(){
        return "Please insert a number between 1 and 6";
    }
}
