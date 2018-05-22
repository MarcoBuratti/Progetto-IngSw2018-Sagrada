package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.PlacementCheck;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.ColorRestriction;
import it.polimi.ingsw.model.restriction.ValueRestriction;

public class SpecialPlacementTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;
    
    private boolean notCheckValue;
    private boolean notCheckColor;
    private boolean notCheckNeighbours;

    public SpecialPlacementTool(boolean notCheckValue,boolean notCheckColor,boolean notCheckNeighbours,ToolNames toolName){
        this.notCheckValue=notCheckValue;
        this.notCheckColor=notCheckColor;
        this.notCheckNeighbours=notCheckNeighbours;
        this.needPlacement=false;
        this.toolName=toolName;
        this.isAlreadyUsed=false;
    }
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed=alreadyUsed;

    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if(!turn.isPlacementDone()) {
            int row =playerMove.getIntParameters(0);
            int column= playerMove.getIntParameters(1);
            Die die=turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
            if (specialCheck(row,column,die,turn.getPlayer().getDashboard().getMatrixScheme())) {
                try {
                    turn.getPlayer().getDashboard().setDieOnCell(row, column, die);
                    turn.getGameBoard().getDraftPool().remove(playerMove.getIndexDie());
                    turn.setTurnIsOver(true);
                    return true;
                } catch (NotValidParametersException | OccupiedCellException e) {
                    e.printStackTrace();
                }

                }
        }
        return false;
    }

    public Color getColor() {
        return this.toolName.getColor();
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }


    public boolean specialCheck(int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();

        if (notCheckColor)
            if (!(matrixScheme[row][column].getRestriction() instanceof ColorRestriction))
                if (!matrixScheme[row][column].allowedMove(myDie))
                    return false;

        if (notCheckValue)
            if (!(matrixScheme[row][column].getRestriction() instanceof ValueRestriction))
                if(!matrixScheme[row][column].allowedMove(myDie))
                    return false;
        if(placementCheck.allowedNeighbours(row, column, myDie, matrixScheme))
            if (notCheckNeighbours)
                return true;
            else
                if(placementCheck.nearBy(row,column,matrixScheme))
                    return true;
        return false;
    }
}
