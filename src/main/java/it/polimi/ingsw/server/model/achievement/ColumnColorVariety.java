package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;

import java.util.EnumMap;
import java.util.Map;

public class ColumnColorVariety implements CardAchievement, AbstractColorVariety {

    private Map<Color, Boolean> counter = new EnumMap<>(Color.class);

    /**
     * Returns an int representing the score effect associated with the Column Color Variety Achievement.
     * The return value is the number of columns with no repeated colors multiplied by five.
     *
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundColumn = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();


        for (int i = 0; i < 5; i++) {
            for (Color color : Color.values())
                counter.put(color, false);
            for (int j = 0; j < 4; j++) {
                foundColumn = found(j, i, matrixScheme, counter, foundColumn);
            }
            if (foundColumn)
                score += 5;
            foundColumn = true;

        }
        return score;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Column_Color_Variety";
    }
}
