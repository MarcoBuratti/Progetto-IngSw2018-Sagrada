package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.PlacementCheck;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;

import java.util.*;
import java.util.stream.Collectors;

public class PlacementMove {

    private Player player;
    private GameBoard gameboard;
    private int row;
    private int column;
    private Die die;

    public PlacementMove(Player player, int row,int column,Die die) {
        this.player = player;
        this.row=row;
        this.column=column;
        this.die=die;
        }

    public boolean placeDie() throws OccupiedCellException, NotValidParametersException {

        PlacementCheck placementCheck = new PlacementCheck();
            if(placementCheck.genericCheck(this.row,this.column,die,player.getDashboard().getMatrixScheme())) {
                player.getDashboard().setDieOnCell(this.row, this.column, die);
                return true;
            }
            else
                return false;

        }
    }

