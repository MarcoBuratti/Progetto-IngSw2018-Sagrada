package it.polimi.ingsw.server.model.exception;

public class OccupiedCellException extends Exception {
    public OccupiedCellException() { super();}

    @Override
    public String toString(){
        return "The cell is occupied!";
    }
}
