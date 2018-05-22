package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.ColorRestriction;
import it.polimi.ingsw.model.restriction.ValueRestriction;

public class ToolReplaceDie implements Tool {

    private boolean isAlreadyUsed;
    private boolean needPlacement;
    private ToolNames toolName;

    private boolean notCheckValue;
    private boolean notCheckColor;
    private boolean needRoundTrack;

    public ToolReplaceDie(boolean notCheckColor,boolean notCheckValue,boolean needRoundTrack,boolean needPlacement,ToolNames toolName) {
        this.toolName=toolName;
        this.notCheckColor=notCheckColor;
        this.notCheckValue=notCheckValue;
        this.needRoundTrack=needRoundTrack;
        this.needPlacement=needPlacement;
        this.isAlreadyUsed = false;

    }

    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;
    }

    public boolean toolEffect(Turn turn,PlayerMove playerMove){
        boolean ret;
        ret = replaceDie(turn,playerMove.getIntParameters(0),playerMove.getIntParameters(1), playerMove.getIntParameters(2), playerMove.getIntParameters(3));
        if(playerMove.getTwoReplace()&& ret)
            ret = replaceDie(turn,playerMove.getIntParameters(4), playerMove.getIntParameters(5), playerMove.getIntParameters(6), playerMove.getIntParameters(7));
        return ret;

    }
    public Color getColor() {
        return this.toolName.getColor();
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }

    public boolean specialCheck(Turn turn,int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();

        if (notCheckColor)
            if (!(matrixScheme[row][column].getRestriction() instanceof ColorRestriction))
                if (!matrixScheme[row][column].allowedMove(myDie))
                    return false;

        if (notCheckValue)
            if (!(matrixScheme[row][column].getRestriction() instanceof ValueRestriction))
                if(!matrixScheme[row][column].allowedMove(myDie))
                    return false;
        if (needRoundTrack)
            if (!turn.getGameBoard().getRoundTrack().isColorOnRoundTrack(myDie.getColor()) && !matrixScheme[row][column].allowedMove(myDie))
                return false;
        if((!placementCheck.nearBy(row,column,matrixScheme))&&(!placementCheck.allowedNeighbours(row,column,myDie,matrixScheme)))
            return false;
        return true;
    }

    private boolean replaceDie(Turn turn, int oldRow,int oldColumn,int newRow,int newColumn){
        Die die = turn.getPlayer().getDashboard().getMatrixScheme()[oldRow][oldColumn].getDie();
        if (!specialCheck(turn,newRow, newColumn, die, turn.getPlayer().getDashboard().getMatrixScheme()))
            return false;
        else {

            try {
                turn.getPlayer().getDashboard().removeDieFromCell(oldRow,oldColumn);
                turn.getPlayer().getDashboard().setDieOnCell(newRow,newColumn,die);
            } catch (NotValidParametersException|OccupiedCellException e) {
                e.printStackTrace();
            }
        }

        return true;


    }
}
