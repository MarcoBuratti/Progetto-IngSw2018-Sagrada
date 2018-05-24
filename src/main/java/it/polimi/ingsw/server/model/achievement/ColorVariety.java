package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

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

    @Override
    public String toString() {
        return "Color Variety\nSets of one of each color anywhere.\n";
    }
}