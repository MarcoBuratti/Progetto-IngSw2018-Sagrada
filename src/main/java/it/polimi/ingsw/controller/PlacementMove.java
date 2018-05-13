package it.polimi.ingsw.controller;

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
    private int indexDieOnDiceStock;

    public PlacementMove(String input, Player player, GameBoard gameBoard) {
        this.player = player;
        this.gameboard = gameBoard;
        this.row=Integer.parseInt(input.substring(0,input.indexOf(" ")));
        input=input.substring(input.indexOf("")+1);
        this.column=Integer.parseInt(input.substring(0,input.indexOf("")));
        input=input.substring(input.indexOf("")+1);
        this.indexDieOnDiceStock=Integer.parseInt(input);
    }

    public boolean positionDie() throws OccupiedCellException, NotValidParametersException {

        Die die = this.gameboard.getDraftPool().get(this.indexDieOnDiceStock);
            PlacementCheck placementCheck = new PlacementCheck();
            if(placementCheck.genericCheck(this.row,this.column,die,player.getDashboard().getMatrixScheme())) {
                player.getDashboard().setDieOnCell(this.row, this.column, die);
                this.gameboard.getDraftPool().remove(this.indexDieOnDiceStock);
                return true;
            }
            else
                return false;

        }
    }

