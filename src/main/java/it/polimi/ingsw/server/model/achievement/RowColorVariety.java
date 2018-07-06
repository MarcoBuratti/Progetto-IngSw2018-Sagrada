package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;

import java.util.EnumMap;
import java.util.Map;

public class RowColorVariety implements CardAchievement, AbstractColorVariety {

    private Map<Color, Boolean> counter = new EnumMap<>(Color.class);

    /**
     * Returns an int representing the score effect associated with the Row Color Variety Achievement.
     * The return value is the number of rows with no repeated colors multiplied by six.
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundRow = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();


        for (int i = 0; i < 4; i++) {
            for (Color color : Color.values())
                counter.put(color, false);
            for (int j = 0; j < 5; j++) {
                foundRow = found( i, j, matrixScheme, counter, foundRow );
            }
            if (foundRow)
                score += 6;
            foundRow = true;

        }
        return score;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Row_Color_Variety";
    }

}