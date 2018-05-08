package it.polimi.ingsw.model.exception;

public class NotValidNumberException extends Exception {
    public NotValidNumberException(){
        super();
    }

    @Override
    public String toString(){
        return "Please insert a number between 1 and 6";
    }
}
