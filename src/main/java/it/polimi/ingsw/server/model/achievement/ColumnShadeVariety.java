package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;

public class ColumnShadeVariety implements CardAchievement {

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        boolean foundColumn = true;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        boolean[] counter = new boolean[6];


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++)
                counter[j] = false;
            for (int j = 0; j < 4; j++)
                if (matrixScheme[j][i].getUsedCell()) {
                    int numberDie = matrixScheme[j][i].getDie().getNumber();
                    if (counter[numberDie - 1])
                        foundColumn = false;
                    else
                        counter[numberDie - 1] = true;
                } else
                    foundColumn = false;
            if (foundColumn)
                score += 4;
            foundColumn = true;

        }
        return score;

    }

    @Override
    public String toString() {
        return "Column Shade Variety: Columns with no repeated values.\n";
    }
}
