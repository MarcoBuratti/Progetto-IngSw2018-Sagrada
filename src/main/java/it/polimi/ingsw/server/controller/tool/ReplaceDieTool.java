package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.RestrictionType;

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
        int oldRow1, oldColumn1, newRow1, newColumn1;
        oldRow1 = playerMove.getIntParameters(0);
        oldColumn1 = playerMove.getIntParameters(1);
        newRow1 = playerMove.getIntParameters(2);
        newColumn1 = playerMove.getIntParameters(3);

        if ( wrongCoordinatesCheck( oldRow1, oldColumn1, newRow1, newColumn1 ) )
            return false;

        if (playerMove.getTwoReplace().isPresent()) {

            if ( playerMove.getTwoReplace().get() ) {

                int oldRow2, oldColumn2, newRow2, newColumn2;
                oldRow2 = playerMove.getIntParameters(4);
                oldColumn2 = playerMove.getIntParameters(5);
                newRow2 = playerMove.getIntParameters(6);
                newColumn2 = playerMove.getIntParameters(7);

                if ( wrongCoordinatesCheck( oldRow2, oldColumn2, newRow2, newColumn2 ) || wrongCoordinatesCheck( oldRow2, oldColumn2, newRow1, newColumn1 ) )
                    return false;

                if ( toolName.equals(ToolNames.TAP_WHEEL) )

                    if (!turn.getPlayer().getDashboard().getMatrixScheme()[oldRow1][oldColumn1].getDie().getColor().
                            equals(turn.getPlayer().getDashboard().getMatrixScheme()[ oldRow2 ][ oldColumn2 ].getDie().getColor()))

                        return false;

                ret = replaceDie(turn, oldRow1, oldColumn1, newRow1, newColumn1);

                if ( ret ) {

                    ret = replaceDie(turn, oldRow2, oldColumn2, newRow2, newColumn2);

                    if (!ret) {

                        try {

                            Die die = turn.getPlayer().getDashboard().removeDieFromCell(newRow1, newColumn1);
                            turn.getPlayer().getDashboard().setDieOnCell(oldRow1, oldColumn1, die);

                        } catch (NotValidParametersException | OccupiedCellException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }

            else ret = replaceDie(turn, oldRow1, oldColumn1, newRow1, newColumn1);

            return ret;

        } else
            return false;
    }

    private boolean wrongCoordinatesCheck ( int row1, int column1, int row2, int column2 ) {
        return ( row1 == row2 && column1 == column2 );
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

    private boolean specialCheck(Turn turn, int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();

        if (checkColor &&
                ( matrixScheme[row][column].getRestriction().getType() == RestrictionType.COLOR ) &&
                !matrixScheme[row][column].allowedMove(myDie)) {
            return false;
        }


        if (checkValue &&
                ( matrixScheme[row][column].getRestriction().getType() == RestrictionType.VALUE ) &&
                !matrixScheme[row][column].allowedMove(myDie)) {
            return false;
        }
        if (needRoundTrack)
            if (!turn.getGameBoard().getRoundTrack().isColorOnRoundTrack(myDie.getColor()) && !matrixScheme[row][column].allowedMove(myDie))
                return false;

        return (placementCheck.nearBy(row, column, matrixScheme)) && (placementCheck.allowedNeighbours(row, column, myDie, matrixScheme));
    }

    private boolean replaceDie(Turn turn, int oldRow, int oldColumn, int newRow, int newColumn) {

        try {

            Die dieFromCell = turn.getPlayer().getDashboard().removeDieFromCell(oldRow, oldColumn);

            if (!specialCheck(turn, newRow, newColumn, dieFromCell, turn.getPlayer().getDashboard().getMatrixScheme())) {
                turn.getPlayer().getDashboard().setDieOnCell(oldRow, oldColumn, dieFromCell);
                return false;

            } else
                turn.getPlayer().getDashboard().setDieOnCell(newRow, newColumn, dieFromCell);
        } catch (NotValidParametersException | OccupiedCellException e) {
            return false;
        }

        return true;


    }
}
