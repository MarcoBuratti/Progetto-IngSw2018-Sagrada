package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.OccupiedCellException;

import java.util.*;

public class ColumnColorVariety implements CardAchievement {

    private Map<Color, Boolean> counter = new EnumMap<>(Color.class);

    public int scoreEffect(Dashboard dashboard) throws OccupiedCellException {

        int score = 0;
        boolean foundColumn = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();


        for (int i = 0; i < 5; i++) {
            for (Color color : Color.values())
                counter.put(color, false);
            for (int j = 0; j < 4; j++)
                if (matrixScheme[j][i].getUsedCell()) {
                    Color color = matrixScheme[j][i].getDie().getColor();
                    if (counter.get(color))
                        foundColumn = false;
                    else
                        counter.put(color, true);
                }
                else
                    foundColumn=false;
            if (foundColumn)
                score += 5;
            foundColumn = true;

        }
        return score;

    }

}
