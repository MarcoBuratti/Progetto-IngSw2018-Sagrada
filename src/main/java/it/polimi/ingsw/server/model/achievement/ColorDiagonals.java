package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.PlacementCheck;

public class ColorDiagonals implements CardAchievement {

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        boolean found = false;
        PlacementCheck placementCheck = new PlacementCheck();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    if (i > 0) {
                        if (j > 0 && matrixScheme[i - 1][j - 1].getUsedCell())
                            if (placementCheck.checkDiceColor(matrixScheme[i - 1][j - 1].getDie(), matrixScheme[i][j].getDie()))
                                found = true;

                        if (j < 4 && matrixScheme[i - 1][j + 1].getUsedCell())
                            if (placementCheck.checkDiceColor(matrixScheme[i - 1][j + 1].getDie(), matrixScheme[i][j].getDie()))
                                found = true;

                    }
                    if (i < 3) {
                        if (j > 0 && matrixScheme[i + 1][j - 1].getUsedCell())
                            if (placementCheck.checkDiceColor(matrixScheme[i + 1][j - 1].getDie(), matrixScheme[i][j].getDie()))
                                found = true;
                        if (j < 4 && matrixScheme[i + 1][j + 1].getUsedCell())
                            if (placementCheck.checkDiceColor(matrixScheme[i + 1][j + 1].getDie(), matrixScheme[i][j].getDie()))
                                found = true;

                    }
                    if (found) {
                        score++;
                        found = false;
                    }
                }
        return score;

    }

    @Override
    public String toString() {
        return "Color Diagonals\nCount of diagonally adjacent same color dice.\n";
    }
}
