package it.polimi.ingsw.server.controller.action;

import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

public class PlacementMove {

    private Player player;
    private GameBoard gameboard;
    private int row;
    private int column;
    private Die die;

    public PlacementMove(Player player, int row, int column, Die die) {
        this.player = player;
        this.row = row;
        this.column = column;
        this.die = die;
    }

    public boolean placeDie() throws OccupiedCellException, NotValidParametersException {

        PlacementCheck placementCheck = new PlacementCheck();
        if (placementCheck.genericCheck(this.row, this.column, die, player.getDashboard().getMatrixScheme())) {
            player.getDashboard().setDieOnCell(this.row, this.column, die);
            return true;
        } else
            return false;

    }

    public Die getDie() {
        return this.die;
    }
}
