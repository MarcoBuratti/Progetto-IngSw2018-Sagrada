package it.polimi.ingsw.server.model.exception;

public class OccupiedCellException extends Exception {

    /**
     * Creates an exception that specifies that the cell is already occupied.
     * Usually it is launched when trying to place a die on an occupied cell.
     */
    public OccupiedCellException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "The cell is occupied!";
    }
}
