package it.polimi.ingsw.server.model;


public class PlacementCheck {

    private static final int ROW = 4;
    private static final int COLUMN = 5;

    /**
     * Returns a boolean specifying whether the matrix is empty or not.
     *
     * @param matrixScheme the matrix of Cell Objects selected by the user
     * @return a boolean
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
     * Returns a boolean which specifies whether two dice have the same color or not.
     *
     * @param die1 the first Die Object
     * @param die2 the second Die Object
     * @return a boolean
     */
    public boolean checkDiceColor(Die die1, Die die2) {
        return (die1.getColor() == die2.getColor());
    }

    /**
     * Returns a boolean which specifies whether two dice have the same value or not.
     *
     * @param die1 the first Die Object
     * @param die2 the second Die Object
     * @return a boolean
     */
    boolean checkDiceNumber(Die die1, Die die2) {
        return (die1.getNumber() == die2.getNumber());
    }

    /**
     * Checks if any of the dice placed near the selected position ( diagonals are not considered ) have the same color
     * or value as the selected die.
     *
     * @param row          the row of the considered position
     * @param column       the column of the considered position
     * @param myDie        the die the user wants to place
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean specifying whether there is any die having the same color or value as myDie parameter placed
     * near the selected position ( coordinates: row, column ) on matrixScheme or not. Diagonals are not considered.
     */
    public boolean allowedNeighbours(int row, int column, Die myDie, Cell[][] matrixScheme) {

        return allowedNeighboursColumn(row, column, myDie, matrixScheme) && allowedNeighboursRow(row, column, myDie, matrixScheme);

    }

    /**
     * This private method checks if any of the dice placed on the same column as the column parameter have
     * the same color or value as the selected die.
     *
     * @param row          the row of the considered position
     * @param column       the column of the considered position
     * @param myDie        the die the user wants to place
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean
     */
    private boolean allowedNeighboursColumn(int row, int column, Die myDie, Cell[][] matrixScheme) {
        if (row > 0 && matrixScheme[row - 1][column].getUsedCell()) {
            Die die = matrixScheme[row - 1][column].getDie();
            if (checkDiceColor(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (row < 3 && matrixScheme[row + 1][column].getUsedCell()) {
            Die die = matrixScheme[row + 1][column].getDie();
            return !checkDiceColor(myDie, die) && !checkDiceNumber(myDie, die);
        }
        return true;
    }

    /**
     * This private method checks if any of the dice placed on the same row as the column parameter have
     * the same color or value as the selected die.
     *
     * @param row          the row of the considered position
     * @param column       the column of the considered position
     * @param myDie        the die the user wants to place
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean
     */
    private boolean allowedNeighboursRow(int row, int column, Die myDie, Cell[][] matrixScheme) {
        if (column > 0 && matrixScheme[row][column - 1].getUsedCell()) {
            Die die = matrixScheme[row][column - 1].getDie();
            if (checkDiceColor(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (column < 4 && matrixScheme[row][column + 1].getUsedCell()) {
            Die die = matrixScheme[row][column + 1].getDie();
            return !checkDiceColor(myDie, die) && !checkDiceNumber(myDie, die);
        }
        return true;
    }


    /**
     * Returns a boolean which specifies if the selected coordinates are acceptable for the player's first placement move.
     * If the matrix is empty, you can place a die only along the borders.
     *
     * @param row    the row of the position where the user wants to place his die
     * @param column the column of the position where the user wants to place his die
     * @return a boolean
     */
    public boolean firstMove(int row, int column) {
        return row == 0 || row == 3 || column == 0 || column == 4;
    }

    /**
     * Checks if any of the cells near the selected position ( diagonals considered ) is occupied.
     *
     * @param row          the row of the considered position
     * @param column       the column of the considered position
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean
     */
    public boolean neighbourOccupiedCell(int row, int column, Cell[][] matrixScheme) {

        boolean cond1;
        boolean cond2;
        boolean cond3;

        if (row > 0) {

            cond1 = matrixScheme[row - 1][column].getUsedCell();
            cond2 = column > 0 && (matrixScheme[row - 1][column - 1].getUsedCell() || matrixScheme[row][column - 1].getUsedCell());
            cond3 = column < 4 && (matrixScheme[row - 1][column + 1].getUsedCell() || matrixScheme[row][column + 1].getUsedCell());

            if (cond1 || cond2 || cond3)
                return true;
        }

        if (row < 3) {

            cond1 = matrixScheme[row + 1][column].getUsedCell();
            cond2 = column > 0 && (matrixScheme[row + 1][column - 1].getUsedCell() || matrixScheme[row][column - 1].getUsedCell());
            cond3 = column < 4 && (matrixScheme[row + 1][column + 1].getUsedCell() || matrixScheme[row][column + 1].getUsedCell());

            return cond1 || cond2 || cond3;
        }

        return false;
    }

    /**
     * Checks if it's possible to place a generic die on the selected position of the matrix.
     * As we're considering a generic die, we're not checking if the die is compatible with the restriction on the selected cell.
     *
     * @param row          the row of the considered position
     * @param column       the column of the considered position
     * @param matrixScheme the matrix of Cell Objects
     * @return a boolean
     */
    public boolean nearBy(int row, int column, Cell[][] matrixScheme) {


        if (isEmpty(matrixScheme)) {

            return firstMove(row, column);

        } else {

            return neighbourOccupiedCell(row, column, matrixScheme);

        }
    }

    /**
     * Checks whether it's possible to place the selected die on the selected position of the matrix or not.
     *
     * @param row          the row of the considered position
     * @param column       the column of the considered position
     * @param matrixScheme the matrix of Cell Objects
     * @param myDie        the die the user wants to place on the selected position
     * @return a boolean
     */
    public boolean genericCheck(int row, int column, Die myDie, Cell[][] matrixScheme) {

        if (!matrixScheme[row][column].allowedMove(myDie))
            return false;
        if (!nearBy(row, column, matrixScheme))
            return false;

        return allowedNeighbours(row, column, myDie, matrixScheme);
    }

}

