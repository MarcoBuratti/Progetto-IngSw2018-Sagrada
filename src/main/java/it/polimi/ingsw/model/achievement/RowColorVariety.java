package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import java.util.*;

public class RowColorVariety implements CardAchievement {

    private Map<Colour, Boolean> counter = new EnumMap<>(Colour.class);

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundRow = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();


        for (int i = 0; i < 4; i++) {
            for (Colour colour : Colour.values())
                counter.put(colour, false);
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    Colour colour = matrixScheme[i][j].getDie().getColour();
                    if (counter.get(colour))
                        foundRow = false;
                    else
                        counter.put(colour, true);
                }
                else
                    foundRow=false;
            if (foundRow)
                score += 6;
            foundRow = true;

        }
        return score;

    }

}