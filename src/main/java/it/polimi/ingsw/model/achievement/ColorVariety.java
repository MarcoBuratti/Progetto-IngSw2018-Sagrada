package it.polimi.ingsw.model.achievement;

import java.util.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public class ColorVariety implements CardAchievement {

    private Map<Color, Integer> counter = new EnumMap<>(Color.class);

    @Override
    public int scoreEffect(Dashboard dashboard) throws OccupiedCellException {

        Cell[][] matrixScheme = dashboard.getMatrixScheme();

        for (Color color : Color.values())
            counter.put(color, 0);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    Color color = matrixScheme[i][j].getDie().getColor();
                    counter.put(color, counter.get(color) + 1);
                }


        return Collections.min(counter.values()) * 4;

    }

}