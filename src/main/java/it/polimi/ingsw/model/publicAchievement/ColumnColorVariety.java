package it.polimi.ingsw.model.publicAchievement;

import it.polimi.ingsw.model.*;
import java.util.*;

public class ColumnColorVariety implements CardAchievement {

    private Map<Colour, Boolean> counter = new EnumMap<>(Colour.class);

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundColumn = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();


        for (int i = 0; i < 5; i++) {
            for (Colour colour : Colour.values())
                counter.put(colour, false);
            for (int j = 0; j < 4; j++)
                if (matrixScheme[j][i].getUsedCell()) {
                    Colour colour = matrixScheme[j][i].getDie().getColour();
                    if (counter.get(colour))
                        foundColumn = false;
                    else
                        counter.put(colour, true);
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
