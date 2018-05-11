package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.OccupiedCellException;

import java.util.*;

public class RowColorVariety implements CardAchievement {

    private Map<Color, Boolean> counter = new EnumMap<>(Color.class);

    public int scoreEffect(Dashboard dashboard) throws OccupiedCellException {

        int score = 0;
        boolean foundRow = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();


        for (int i = 0; i < 4; i++) {
            for (Color color : Color.values())
                counter.put(color, false);
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    Color color = matrixScheme[i][j].getDie().getColor();
                    if (counter.get(color))
                        foundRow = false;
                    else
                        counter.put(color, true);
                }
                else
                    foundRow=false;
            if (foundRow)
                score += 6;
            foundRow = true;

        }
        return score;

    }

    @Override
    public String toString() {
        return "Row Color Variety\nRows with no repeated colors.\n";
    }

}