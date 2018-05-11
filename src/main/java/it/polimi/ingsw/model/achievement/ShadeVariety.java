package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.OccupiedCellException;

import java.util.*;

public class ShadeVariety implements CardAchievement {


    public int scoreEffect(Dashboard dashboard) throws OccupiedCellException {

        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        Integer[] counter = new Integer[6];

        for (int i=0; i < 6; i++)
            counter[i] = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    int numberDie = matrixScheme[i][j].getDie().getNumber();
                    counter[numberDie - 1]++;
                }


        }
        return Collections.min(Arrays.asList(counter))*5;

    }

    @Override
    public String toString() {
        return "Shades Variety\nSets of one of each value anywhere.\n";
    }
}