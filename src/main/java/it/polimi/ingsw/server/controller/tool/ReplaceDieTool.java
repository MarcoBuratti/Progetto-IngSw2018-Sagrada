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

public class ReplaceDieTool implements Tool {

    private boolean isAlreadyUsed;
    private boolean needPlacement;
    private ToolNames toolName;

    private boolean checkValue;
    private boolean checkColor;
    private boolean needRoundTrack;

    /**
     * Creates a ChangeDieTool, a class used to manage the following tools: COPPER FOIL BURNISHER, EGLOMISE BRUSH, LATHEKIN, TAP WHEEL.
     *
     * @param checkColor     a boolean specifying whether the color restriction must be considered or not
     * @param checkValue     a boolean specifying whether the value restriction must be considered or not
     * @param needRoundTrack a boolean specifying whether information about the round track is needed or not
     * @param needPlacement  a boolean which specifies whether the tool has already been used once or not
     * @param toolName       an instance of ToolNames enum representing the tool's name
     */
    ReplaceDieTool(boolean checkColor, boolean checkValue, boolean needRoundTrack, boolean needPlacement, ToolNames toolName) {
        this.toolName = toolName;
        this.checkColor = checkColor;
        this.checkValue = checkValue;
        this.needRoundTrack = needRoundTrack;
        this.needPlacement = needPlacement;
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
     * Returns an array of int containing the coordinates (old row, old column, new row, new column) for the die the player wants to move.
     * Returns an empty array if the index is different from 1 or 2.
     *
     * @param index an int specifying if the user wants to read the coordinates referred to the first or to the second die
     * @param playerMove the PlayerMove Object representing the move
     * @return an array of int containing coordinates
     */
    private int[] getCoordinates(int index, PlayerMove playerMove) {
        if (index == 1)
            return new int[]{playerMove.getIntParameters(0), playerMove.getIntParameters(1), playerMove.getIntParameters(2), playerMove.getIntParameters(3)};
        else if (index == 2)
            return new int[]{playerMove.getIntParameters(4), playerMove.getIntParameters(5), playerMove.getIntParameters(6), playerMove.getIntParameters(7)};
        else return new int[]{};
    }

    /**
     * Tries to move the selected dice and returns a boolean specifying whether both move have been made successfully or not.
     *
     * @param turn the turn being played
     * @param coordinates1 an array of int containing coordinates referred to the first die
     * @param coordinates2 an array of int containing coordinates referred to the second die
     * @return a boolean
     */
    private boolean tryReplace(Turn turn, int[] coordinates1, int[] coordinates2) {
        boolean ret = replaceDie(turn, coordinates1[0], coordinates1[1], coordinates1[2], coordinates1[3]);

        if (ret) {

            ret = replaceDie(turn, coordinates2[0], coordinates2[1], coordinates2[2], coordinates2[3]);

            if (!ret) {

                try {

                    Die die = turn.getPlayer().getDashboard().removeDieFromCell(coordinates1[2], coordinates1[3]);
                    turn.getPlayer().getDashboard().setDieOnCell(coordinates1[0], coordinates1[1], die);

                } catch (NotValidParametersException | OccupiedCellException e) {
                    e.printStackTrace();
                }

            }

        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean toolEffect(Turn turn, PlayerMove playerMove) {

        boolean ret;
        int[] coordinates1 = getCoordinates(1, playerMove);

        if (wrongCoordinatesCheck(coordinates1[0], coordinates1[1], coordinates1[2], coordinates1[3]))
            return false;

        Optional<Boolean> twoDiceMove = playerMove.getTwoReplace();

        if (twoDiceMove.isPresent()) {

            boolean twoDiceMoveValue = twoDiceMove.get();

            if (twoDiceMoveValue) {

                int[] coordinates2 = getCoordinates(2, playerMove);

                if (wrongCoordinatesCheck(coordinates2[0], coordinates2[1], coordinates2[2], coordinates2[3]) || wrongCoordinatesCheck(coordinates2[0], coordinates2[1], coordinates1[2], coordinates1[3])
                        || (toolName.equals(ToolNames.TAP_WHEEL) && !turn.getPlayer().getDashboard().getMatrixScheme()[coordinates1[0]][coordinates1[1]].getDie().getColor().
                        equals(turn.getPlayer().getDashboard().getMatrixScheme()[coordinates2[0]][coordinates2[1]].getDie().getColor())))
                    return false;

                ret = tryReplace(turn, coordinates1, coordinates2);
            } else ret = replaceDie(turn, coordinates1[0], coordinates1[1], coordinates1[2], coordinates1[3]);

            return ret;

        } else
            return false;
    }

    /**
     * Returns a boolean specifying if the given coordinates are incorrect.
     *
     * @param row1    the first row index
     * @param column1 the second row index
     * @param row2    the second row index
     * @param column2 the second column index
     * @return a boolean
     */
    private boolean wrongCoordinatesCheck(int row1, int column1, int row2, int column2) {
        return (row1 == row2 && column1 == column2);
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
     * @param turn         the turn being played
     * @param row          the row of the selected position
     * @param column       the column of the selected position
     * @param myDie        the die the player wants to place
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean
     */
    private boolean specialCheck(Turn turn, int row, int column, Die myDie, Cell[][] matrixScheme) {

        PlacementCheck placementCheck = new PlacementCheck();

        if (checkColor &&
                (matrixScheme[row][column].getRestriction().getType() == RestrictionType.COLOR) &&
                !matrixScheme[row][column].allowedMove(myDie))
            return false;

        if (checkValue &&
                (matrixScheme[row][column].getRestriction().getType() == RestrictionType.VALUE) &&
                !matrixScheme[row][column].allowedMove(myDie))
            return false;

        if (needRoundTrack && (!turn.getGameBoard().getRoundTrack().isColorOnRoundTrack(myDie.getColor()) || !matrixScheme[row][column].allowedMove(myDie)))
            return false;

        return (placementCheck.nearBy(row, column, matrixScheme)) && (placementCheck.allowedNeighbours(row, column, myDie, matrixScheme));
    }

    /**
     * Returns a boolean specifying if the die has been moved successfully.
     *
     * @param turn      the turn being played
     * @param oldRow    the row index of the position where the die is currently placed
     * @param oldColumn the column index of the position where the die is currently placed
     * @param newRow    the row index of the position where the die must be moved
     * @param newColumn the column index of the position where the die must be moved
     * @return a boolean
     */
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
