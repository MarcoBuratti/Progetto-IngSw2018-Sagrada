package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;

interface AbstractShadeVariety {

    /**
     * Checks whether the value of the selected die is present in counter or not.
     * Returns the new value of the boolean flag found.
     *
     * @param i            the row index of the cell the user wants to check
     * @param j            the column index of the cell the user wants to check
     * @param matrixScheme the matrix of Cell Objects
     * @param counter      a boolean array specifying whether any value has been already found on the row/column or not
     * @param found        a boolean flag
     * @return a boolean flag
     */
    default boolean found(int i, int j, Cell[][] matrixScheme, boolean[] counter, boolean found) {

        boolean newFound = found;

        if (matrixScheme[i][j].getUsedCell()) {
            int dieValue = matrixScheme[i][j].getDie().getNumber();
            if (counter[dieValue - 1])
                newFound = false;
            else
                counter[dieValue - 1] = true;
        } else
            newFound = false;

        return newFound;
    }
}
