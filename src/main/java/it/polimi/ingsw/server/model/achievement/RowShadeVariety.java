package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;


public class RowShadeVariety implements CardAchievement, AbstractShadeVariety {

    /**
     * Returns an int representing the score effect associated with the Row Shade Variety Achievement.
     * The return value is the number of rows with no repeated values multiplied by five.
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundRow = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        boolean[] counter = new boolean[6];


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++)
                counter[j] = false;
            for (int j = 0; j < 5; j++) {
                foundRow = found( i, j, matrixScheme, counter, foundRow );
            }
            if (foundRow)
                score += 5;
            foundRow = true;

        }
        return score;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Row_Shades_Variety";
    }
}