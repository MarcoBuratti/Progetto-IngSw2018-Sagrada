package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.ColorRestriction;
import it.polimi.ingsw.model.restriction.ValueRestriction;

public class ToolReplaceDie implements Tool {

    private boolean isAlreadyUsed;
    private Color color;

    private boolean notCheckValue;
    private boolean notCheckColor;
    private boolean needRoundTrack;
    private  Turn turn;
    private int oldrow1;
    private int oldcolumn1;
    private int newrow1;
    private int newcolumn1;
    private int oldrow2;
    private int oldcolumn2;
    private int newrow2;
    private int newcolumn2;
    private boolean twoReplace;

    public ToolReplaceDie(boolean notCheckColor,boolean notCheckValue,boolean needRoundTrack) {
        this.notCheckColor=notCheckColor;
        this.notCheckValue=notCheckValue;
        this.needRoundTrack=needRoundTrack;
        this.isAlreadyUsed = false;

    }


    @Override
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    @Override
    public void setAlreadyUsed() {
        this.isAlreadyUsed = true;
    }

    @Override
    public boolean toolEffect(Turn turn) throws OccupiedCellException, NotValidParametersException {
        Die die = turn.getPlayer().getDashboard().getMatrixScheme()[oldrow1][oldcolumn1].getDie();
        if (!specialCheck(newrow1,newcolumn1,die,turn.getPlayer().getDashboard().getMatrixScheme()))
            return false;
        else {
            die = turn.getPlayer().getDashboard().removeDieFromCell(oldrow1,oldcolumn1);
            turn.getPlayer().getDashboard().setDieOnCell(newrow1,newcolumn1,die);
        }
        if(this.twoReplace) {
            die = turn.getPlayer().getDashboard().getMatrixScheme()[oldrow2][oldcolumn2].getDie();
            if (!specialCheck(newrow2, newcolumn2, die, turn.getPlayer().getDashboard().getMatrixScheme()))
                return false;
            else {
                turn.getPlayer().getDashboard().removeDieFromCell(oldrow2,oldcolumn2);
                turn.getPlayer().getDashboard().setDieOnCell(newrow2,newcolumn2,die);
            }
        }
        return true;

    }

    public void setToolParameters(Turn turn, int oldrow1, int oldcolumn1, int newrow1, int newcolumn1,
                                           int oldrow2, int oldcolumn2, int newrow2, int newcolumn2) {
        this.turn=turn;
        this.oldrow1 = oldrow1;
        this.oldcolumn1 = oldcolumn1;
        this.newrow1 = newrow1;
        this.newcolumn1 = newcolumn1;
        this.oldrow2 = oldrow2;
        this.oldcolumn2 = oldcolumn2;
        this.newrow2 = newrow2;
        this.newcolumn2 = newcolumn2;
        this.twoReplace = true;
    }

    public void setToolParameters(Turn turn,int oldrow1, int oldcolumn1, int newrow1, int newcolumn1) {

        this.turn=turn;
        this.oldrow1 = oldrow1;
        this.oldcolumn1 = oldcolumn1;
        this.newrow1 = newrow1;
        this.newcolumn1 = newcolumn1;
        this.twoReplace = false;
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
        if (needRoundTrack)
            if (!turn.getGameBoard().getRoundTrack().isColorOnRoundTrack(myDie.getColor()) && !matrixScheme[row][column].allowedMove(myDie))
                return false;
        if((!placementCheck.nearBy(row,column,matrixScheme))&&(!placementCheck.allowedNeighbours(row,column,myDie,matrixScheme)))
            return false;
        return true;
    }
}
