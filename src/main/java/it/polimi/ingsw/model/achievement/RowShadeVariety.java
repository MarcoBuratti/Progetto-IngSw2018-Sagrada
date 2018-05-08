package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;



public class RowShadeVariety implements CardAchievement {

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundRow = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        boolean[] counter = new boolean[6];


        for (int i = 0; i < 4; i++) {
            for (int j=0; j < 6; j++)
                counter[j] = false;
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    int numberDie = matrixScheme[i][j].getDie().getNumber();
                    if (counter[numberDie - 1])
                        foundRow = false;
                    else
                        counter[numberDie - 1] = true;
                }
                else
                    foundRow=false;
            if (foundRow)
                score += 5;
            foundRow = true;

        }
        return score;

    }
}