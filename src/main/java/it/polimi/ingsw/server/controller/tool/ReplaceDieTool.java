package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.RestrictionEnum;

import java.util.Arrays;
import java.util.List;

public class ReplaceDieTool implements Tool {

    private boolean isAlreadyUsed;
    private boolean needPlacement;
    private ToolNames toolName;

    private boolean checkValue;
    private boolean checkColor;
    private boolean needRoundTrack;

    public ReplaceDieTool(boolean checkColor, boolean checkValue, boolean needRoundTrack, boolean needPlacement, ToolNames toolName) {
        this.toolName = toolName;
        this.checkColor = checkColor;
        this.checkValue = checkValue;
        this.needRoundTrack = needRoundTrack;
        this.needPlacement = needPlacement;
        this.isAlreadyUsed = false;

    }

    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;
    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        boolean ret;
        if (playerMove.getTwoReplace().isPresent()) {
            if (toolName.equals(ToolNames.TAP_WHEEL) && playerMove.getTwoReplace().get())
                if (!turn.getPlayer().getDashboard().getMatrixScheme()[playerMove.getIntParameters(0)][playerMove.getIntParameters(1)].getDie().getColor().
                        equals(turn.getPlayer().getDashboard().getMatrixScheme()[playerMove.getIntParameters(4)][playerMove.getIntParameters(5)].getDie().getColor()))
                    return false;
            ret = replaceDie(turn, playerMove.getIntParameters(0), playerMove.getIntParameters(1), playerMove.getIntParameters(2), playerMove.getIntParameters(3));
            if (playerMove.getTwoReplace().get() && ret)
                ret = replaceDie(turn, playerMove.getIntParameters(4), playerMove.getIntParameters(5), playerMove.getIntParameters(6), playerMove.getIntParameters(7));
            return ret;
        } else
            throw new IllegalArgumentException();

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

    public boolean specialCheck(Turn turn, int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();
        List<RestrictionEnum> restrictions = Arrays.asList(RestrictionEnum.values());

        if (checkColor &&
                restrictions.indexOf(matrixScheme[row][column].getRestriction()) > 6 &&
                !matrixScheme[row][column].allowedMove(myDie))
            return false;


        if (checkValue &&
                restrictions.indexOf(matrixScheme[row][column].getRestriction()) <= 6 &&
                restrictions.indexOf(matrixScheme[row][column].getRestriction()) > 0 &&
                !matrixScheme[row][column].allowedMove(myDie)) {
            return false;
        }
        if (needRoundTrack)
            if (!turn.getGameBoard().getRoundTrack().isColorOnRoundTrack(myDie.getColor()) && !matrixScheme[row][column].allowedMove(myDie))
                return false;

        if ((!placementCheck.nearBy(row, column, matrixScheme)) || (!placementCheck.allowedNeighbours(row, column, myDie, matrixScheme)))
            return false;
        return true;
    }

    private boolean replaceDie(Turn turn, int oldRow, int oldColumn, int newRow, int newColumn) {
        Die die = turn.getPlayer().getDashboard().getMatrixScheme()[oldRow][oldColumn].getDie();
        try {
            turn.getPlayer().getDashboard().removeDieFromCell(oldRow, oldColumn);
            if (!specialCheck(turn, newRow, newColumn, die, turn.getPlayer().getDashboard().getMatrixScheme())) {
                turn.getPlayer().getDashboard().setDieOnCell(oldRow, oldColumn, die);
                return false;
            } else
                turn.getPlayer().getDashboard().setDieOnCell(newRow, newColumn, die);
        } catch (NotValidParametersException | OccupiedCellException e) {
            e.printStackTrace();
        }

        return true;


    }
}
