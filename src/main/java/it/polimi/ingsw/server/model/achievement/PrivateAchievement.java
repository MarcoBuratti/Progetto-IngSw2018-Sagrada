package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;

public class PrivateAchievement implements CardAchievement {

    private Color color;

    public PrivateAchievement(Color color) {
        this.color = color;
    }

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell())
                    if (this.color == matrixScheme[i][j].getDie().getColor())
                        score += matrixScheme[i][j].getDie().getNumber();

        return score;
    }

    @Override
    public String toString() {
        return "This is a private achievement: Color: " + this.color + "\n";
    }
}
