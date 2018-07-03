package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;

import java.util.Map;

interface AbstractColorVariety {

    /**
     * Checks whether the color of the selected die is present in counter or not.
     * Returns the new value of the boolean flag found.
     * @param i the row index of the cell the user wants to check
     * @param j the column index of the cell the user wants to check
     * @param matrixScheme the matrix of Cell Objects
     * @param counter a map specifying whether any color has been already found on the row/column or not
     * @param found a boolean flag
     * @return a boolean flag
     */
    default boolean found ( int i, int j, Cell[][] matrixScheme, Map<Color, Boolean> counter, boolean found) {

        boolean newFound = found;

        if (matrixScheme[i][j].getUsedCell()) {
            Color color = matrixScheme[i][j].getDie().getColor();
            if (counter.get(color))
                newFound = false;
            else
                counter.put(color, true);
        } else
            newFound = false;

        return newFound;
    }

}
