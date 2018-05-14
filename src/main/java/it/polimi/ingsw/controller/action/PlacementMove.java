package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.PlacementCheck;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public class PlacementMove {

    private Player player;
    private GameBoard gameboard;
    private int row;
    private int column;
    private int indexDieOnDraftPool;

    public PlacementMove(String input, Player player, GameBoard gameBoard) {
        this.player = player;
        this.gameboard = gameBoard;
        String[] scanner= input.split(" ");
        this.row=Integer.parseInt(scanner[0]);
        this.column=Integer.parseInt(scanner[1]);
        this.indexDieOnDraftPool=Integer.parseInt(scanner[2]);
    }

    public int getIndexDieOnDraftPool() {
        return indexDieOnDraftPool;
    }

    public boolean positionDie() throws OccupiedCellException, NotValidParametersException {

        Die die = this.gameboard.getDraftPool().get(this.indexDieOnDraftPool);
            PlacementCheck placementCheck = new PlacementCheck();
            if(placementCheck.genericCheck(this.row,this.column,die,player.getDashboard().getMatrixScheme())) {
                player.getDashboard().setDieOnCell(this.row, this.column, die);
                this.gameboard.removeDieFromDraftPool(die);

                return true;
            }
            else
                return false;

        }
    }

