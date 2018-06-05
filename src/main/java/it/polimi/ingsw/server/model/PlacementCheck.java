package it.polimi.ingsw.server.model;


public class PlacementCheck {

    private static final int ROW = 4;
    private static final int COLUMN = 5;

    /**
     * the isEmpty class checks if a die has already been placed on the matrixScheme,
     * if there are dice return true otherwise return false
     *
     * @param matrixScheme
     * @return boolean
     */
    public boolean isEmpty(Cell[][] matrixScheme) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (matrixScheme[i][j].getUsedCell()) return false;
            }
        }
        return true;
    }

    /**
     * control if two dice has the same color and return true if is the same
     *
     * @param die1
     * @param die2
     * @return
     */
    public boolean checkDiceColor(Die die1, Die die2) {
        return (die1.getColor() == die2.getColor());
    }

    /**
     * control if two dice has the same number, return false if is different
     *
     * @param die1
     * @param die2
     * @return
     */
    public boolean checkDiceNumber(Die die1, Die die2) {
        return (die1.getNumber() == die2.getNumber());
    }

    /**
     * the class allowedNeighbours checks if the "neighbors" (above, below, right, left)
     * have common characters, same color or same number
     * it return false if one the condition is verified, true otherwise
     *
     * @param row
     * @param column
     * @param myDie
     * @param matrixScheme
     * @return
     */
    public boolean allowedNeighbours(int row, int column, Die myDie, Cell[][] matrixScheme) {

        if (row > 0 && matrixScheme[row - 1][column].getUsedCell()) {
            Die die = matrixScheme[row - 1][column].getDie();
            if (checkDiceColor(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (row < 3 && matrixScheme[row + 1][column].getUsedCell()) {
            Die die = matrixScheme[row + 1][column].getDie();
            if (checkDiceColor(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (column > 0 && matrixScheme[row][column - 1].getUsedCell()) {
            Die die = matrixScheme[row][column - 1].getDie();
            if (checkDiceColor(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (column < 4 && matrixScheme[row][column + 1].getUsedCell()) {
            Die die = matrixScheme[row][column + 1].getDie();
            if (checkDiceColor(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        return true;
    }

    /**
     * performs a generic check through the methods previously defined
     * if there are no particular constraints
     *
     * @param row
     * @param column
     * @param myDie
     * @param matrixScheme
     * @return
     */
    public boolean genericCheck(int row, int column, Die myDie, Cell[][] matrixScheme) {

        if (!matrixScheme[row][column].allowedMove(myDie))
            return false;
        if (!nearBy(row, column, matrixScheme))
            return false;

        if (!allowedNeighbours(row, column, myDie, matrixScheme))
            return false;

        return true;
    }

    /**
     * check if there are any neighbors to be able to place the die, it does not control any kind of restriction
     * return true if the die can be place, false if can't
     *
     * @param row
     * @param column
     * @param matrixScheme
     * @return
     */
    public boolean nearBy(int row, int column, Cell[][] matrixScheme) {


        if (isEmpty(matrixScheme)) {
            if (row == 0 || row == 4 || column == 0 || column == 5) {
                return true;
            } else return false;

        } else {

            if (row > 0) {
                if (matrixScheme[row - 1][column].getUsedCell()) return true;
                if (column > 0)
                    if (matrixScheme[row - 1][column - 1].getUsedCell() || matrixScheme[row][column - 1].getUsedCell())
                        return true;
                if (column < 4)
                    if (matrixScheme[row - 1][column + 1].getUsedCell() || matrixScheme[row][column + 1].getUsedCell())
                        return true;
            }
            if (row < 3) {
                if (matrixScheme[row + 1][column].getUsedCell()) return true;
                if (column > 0)
                    if (matrixScheme[row + 1][column - 1].getUsedCell() || matrixScheme[row][column - 1].getUsedCell())
                        return true;
                if (column < 4)
                    if (matrixScheme[row + 1][column + 1].getUsedCell() || matrixScheme[row][column + 1].getUsedCell())
                        return true;
            }
        }
        return false;
    }

}

