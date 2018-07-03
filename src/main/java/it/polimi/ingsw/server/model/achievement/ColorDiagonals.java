package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.PlacementCheck;

public class ColorDiagonals implements CardAchievement {

    /**
     * Returns an int representing the score effect associated with the Color Diagonals Achievement.
     * The return value is the count of the diagonally adjacent same color dice.
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        boolean found;
        PlacementCheck placementCheck = new PlacementCheck();


        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++) {
                if (matrixScheme[i][j].getUsedCell()) {

                    found = firstCondition( i , j, matrixScheme, placementCheck );

                    if (!found)
                        found = secondCondition( i, j, matrixScheme, placementCheck );

                    if (found) {
                        score++;
                    }
                }
            }
        return score;

    }

    /**
     * Returns the first condition of the boolean found in the method scoreEffect.
     * @param i the row index of the cell the user wants to check
     * @param j the column index of the cell the user wants to check
     * @param matrixScheme the matrix of Cell Objects
     * @param placementCheck an instance of PlacementCheck that is used in the method
     * @return a boolean
     */
    private boolean firstCondition (int i, int j, Cell[][] matrixScheme, PlacementCheck placementCheck) {
        return i > 0 &&
                ( ((j > 0 && matrixScheme[i - 1][j - 1].getUsedCell()) && (placementCheck.checkDiceColor(matrixScheme[i - 1][j - 1].getDie(), matrixScheme[i][j].getDie())))
                ||  (((j < 4) && (matrixScheme[i - 1][j + 1].getUsedCell())) && (placementCheck.checkDiceColor(matrixScheme[i - 1][j + 1].getDie(), matrixScheme[i][j].getDie()))) );
    }

    /**
     * Returns the second condition of the boolean found in the method scoreEffect.
     * @param i the row index of the cell the user wants to check
     * @param j the column index of the cell the user wants to check
     * @param matrixScheme the matrix of Cell Objects
     * @param placementCheck an instance of PlacementCheck that is used in the method
     * @return a boolean
     */
    private boolean secondCondition (int i, int j, Cell[][] matrixScheme, PlacementCheck placementCheck) {
        return i < 3 &&
                ( ((j > 0 && matrixScheme[i + 1][j - 1].getUsedCell()) && (placementCheck.checkDiceColor(matrixScheme[i + 1][j - 1].getDie(), matrixScheme[i][j].getDie())))
                || ((j < 4 && matrixScheme[i + 1][j + 1].getUsedCell()) && (placementCheck.checkDiceColor(matrixScheme[i + 1][j + 1].getDie(), matrixScheme[i][j].getDie()))) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Color Diagonals: Count of diagonally adjacent same color dice.!";
    }
}
