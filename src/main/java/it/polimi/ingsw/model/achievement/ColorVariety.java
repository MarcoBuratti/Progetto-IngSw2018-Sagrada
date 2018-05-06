package it.polimi.ingsw.model.achievement;

import java.util.*;
import it.polimi.ingsw.model.*;

public class ColorVariety implements CardAchievement {

    private Map<Colour, Integer> counter = new EnumMap<>(Colour.class);

    @Override
    public int scoreEffect(Dashboard dashboard) {

        Cell[][] matrixScheme = dashboard.getMatrixScheme();

        for (Colour colour : Colour.values())
            counter.put(colour, 0);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    Colour colour = matrixScheme[i][j].getDie().getColour();
                    counter.put(colour, counter.get(colour) + 1);
                }


        return Collections.min(counter.values()) * 4;

    }

}