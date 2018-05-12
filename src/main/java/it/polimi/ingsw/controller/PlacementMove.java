package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.action.Placement;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public class PlacementMove implements Runnable {

    private String move;
    private Player player;
    private GameBoard gameboard;

    public PlacementMove(String move, Player player, GameBoard gameBoard) {
        this.player = player;
        this.move = move;
        this.gameboard = gameBoard;
    }

    @Override
    public void run() {

        Placement placement = new Placement(move);
        Die die = this.gameboard.getDraftPool().get(placement.getIndexDieOnDiceStock());
        try {
            player.getDashboard().setDieOnCell(placement.getRow(), placement.getColumn(), die);
            this.gameboard.getDraftPool().remove(placement.getIndexDieOnDiceStock());
        } catch (NotValidParametersException | OccupiedCellException e) {
            e.printStackTrace();
        }
    }
}
