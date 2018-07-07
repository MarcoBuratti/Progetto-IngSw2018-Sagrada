package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.RestrictionType;

import java.util.Optional;

public class SpecialPlacementTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    private boolean checkValue;
    private boolean checkColor;
    private boolean checkNeighbours;

    /**
     * Creates a ChangeDieTool, a class used to manage the following tools: CORK BAKED STRAIGHTEDGE.
     *
     * @param checkColor      a boolean specifying whether the color restriction must be considered or not
     * @param checkValue      a boolean specifying whether the value restriction must be considered or not
     * @param checkNeighbours a boolean specifying whether a check about the cells
     *                        near the one where the user wants to move the die is needed or not
     * @param toolName        an instance of ToolNames enum representing the tool's name
     */
    SpecialPlacementTool(boolean checkValue, boolean checkColor, boolean checkNeighbours, ToolNames toolName) {
        this.checkValue = checkValue;
        this.checkColor = checkColor;
        this.checkNeighbours = checkNeighbours;
        this.needPlacement = false;
        this.toolName = toolName;
        this.isAlreadyUsed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean toolEffect(Turn turn, PlayerMove playerMove) {

        Optional<Integer> dieIndex = playerMove.getIndexDie();

        if (dieIndex.isPresent()) {

            Integer dieIndexValue = dieIndex.get();


            if (dieIndexValue >= turn.getGameBoard().getDraftPool().size())
                return false;

            if (!turn.isPlacementDone()) {
                int row = playerMove.getIntParameters(0);
                int column = playerMove.getIntParameters(1);
                Die die = turn.getGameBoard().getDraftPool().get(dieIndexValue);
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
        } else
            throw new IllegalArgumentException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needPlacement() {
        return this.needPlacement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolNames getToolName() {
        return this.toolName;
    }

    /**
     * Returns a boolean specifying whether it's possible to make the move or not.
     *
     * @param row          the row of the selected position
     * @param column       the column of the selected position
     * @param myDie        the die the player wants to place
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean
     */
    private boolean specialCheck(int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();

        if (checkColor &&
                (matrixScheme[row][column].getRestriction().getType() == RestrictionType.COLOR) &&
                !matrixScheme[row][column].allowedMove(myDie)) {
            return false;
        }


        if (checkValue &&
                (matrixScheme[row][column].getRestriction().getType() == RestrictionType.VALUE) &&
                !matrixScheme[row][column].allowedMove(myDie)) {
            return false;
        }

        if (!checkNeighbours) {
            if (placementCheck.isEmpty(matrixScheme))
                return placementCheck.firstMove(row, column);
            else
                return !placementCheck.neighbourOccupiedCell(row, column, matrixScheme);
        }

        return false;
    }
}
