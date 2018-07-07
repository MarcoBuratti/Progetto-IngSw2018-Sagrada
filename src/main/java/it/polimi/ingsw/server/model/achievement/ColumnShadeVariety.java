package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;

public class ColumnShadeVariety implements CardAchievement, AbstractShadeVariety {

    /**
     * Returns an int representing the score effect associated with the Column Shade Variety Achievement.
     * The return value is the number of columns with no repeated values multiplied by four.
     *
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundColumn = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        boolean[] counter = new boolean[6];


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++)
                counter[j] = false;
            for (int j = 0; j < 4; j++) {
                foundColumn = found(j, i, matrixScheme, counter, foundColumn);
            }
            if (foundColumn)
                score += 4;
            foundColumn = true;

        }
        return score;

    }

    @Override
    public String toString() {
        return "Column_Shade_Variety";
    }
}
