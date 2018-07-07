package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;

import java.util.Arrays;
import java.util.Collections;

public class ShadeVariety implements CardAchievement {

    /**
     * Returns an int representing the score effect associated with the Shade Variety Achievement.
     * The return value is the count of sets of one of each value anywhere multiplied by five.
     *
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        Integer[] counter = new Integer[6];

        for (int i = 0; i < 6; i++)
            counter[i] = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    int numberDie = matrixScheme[i][j].getDie().getNumber();
                    counter[numberDie - 1]++;
                }


        }
        return Collections.min(Arrays.asList(counter)) * 5;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Shades_Variety";
    }
}