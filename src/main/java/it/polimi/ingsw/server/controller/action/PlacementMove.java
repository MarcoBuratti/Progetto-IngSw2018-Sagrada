package it.polimi.ingsw.server.controller.action;

import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

public class PlacementMove {

    private Player player;
    private int row;
    private int column;
    private Die die;

    /**
     * Creates a new PlacementMove Object.
     * This class is used to manages a placement move.
     * @param player the player who's trying to make the move
     * @param row the row of the selected position
     * @param column the column of the selected position
     * @param die the die the player wants to place on the position
     */
    public PlacementMove(Player player, int row, int column, Die die) {
        this.player = player;
        this.row = row;
        this.column = column;
        this.die = die;
    }

    /**
     * Allows the user to place the die on the selected cell if it's possible.
     * @return a boolean specifying whether the move has been done or not
     * @throws OccupiedCellException if the cell is already occupied
     * @throws NotValidParametersException if the parameters sent by the user are not valid
     */
    public boolean placeDie() throws OccupiedCellException, NotValidParametersException {

        PlacementCheck placementCheck = new PlacementCheck();
        if (placementCheck.genericCheck(this.row, this.column, die, player.getDashboard().getMatrixScheme())) {
            player.getDashboard().setDieOnCell(this.row, this.column, die);
            return true;
        } else
            return false;

    }

    /**
     * Returns the die attribute.
     * @return a Die Object
     */
    public Die getDie() {
        return this.die;
    }
}

