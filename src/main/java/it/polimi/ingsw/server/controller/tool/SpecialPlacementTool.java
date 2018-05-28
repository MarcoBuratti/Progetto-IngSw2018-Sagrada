package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.ColorRestriction;
import it.polimi.ingsw.server.model.restriction.ValueRestriction;

public class SpecialPlacementTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    private boolean checkValue;
    private boolean checkColor;
    private boolean checkNeighbours;

    public SpecialPlacementTool(boolean checkValue, boolean checkColor, boolean checkNeighbours, ToolNames toolName) {
        this.checkValue = checkValue;
        this.checkColor = checkColor;
        this.checkNeighbours = checkNeighbours;
        this.needPlacement = false;
        this.toolName = toolName;
        this.isAlreadyUsed = false;
    }

    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;

    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if (!turn.isPlacementDone()) {
            int row = playerMove.getIntParameters(0);
            int column = playerMove.getIntParameters(1);
            Die die = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
            if (specialCheck(row, column, die, turn.getPlayer().getDashboard().getMatrixScheme())) {
                try {
                    turn.getPlayer().getDashboard().setDieOnCell(row, column, die);
                    turn.getGameBoard().removeDieFromDraftPool(die);
                    turn.setTurnIsOver();
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

    public void placementDie(Turn turn) {

    }

    public ToolNames getToolName() {
        return this.toolName;
    }


    public boolean specialCheck(int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();

        if (checkColor)
            if (!(matrixScheme[row][column].getRestriction() instanceof ColorRestriction))
                if (!matrixScheme[row][column].allowedMove(myDie))
                    return false;

        if (checkValue)
            if (!(matrixScheme[row][column].getRestriction() instanceof ValueRestriction))
                if (!matrixScheme[row][column].allowedMove(myDie))
                    return false;
        if (placementCheck.allowedNeighbours(row, column, myDie, matrixScheme))
            if (checkNeighbours) {
                if (placementCheck.nearBy(row, column, matrixScheme))
                    return true;
            }else
                return true;
        return false;
    }
}
