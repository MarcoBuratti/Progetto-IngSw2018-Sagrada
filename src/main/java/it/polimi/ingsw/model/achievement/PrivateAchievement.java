package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;

public class PrivateAchievement implements CardAchievement {

    private Colour colour;

    public PrivateAchievement(Colour colour){
        this.colour= colour;
    }

    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        Cell[][]matrixScheme= dashboard.getMatrixScheme();

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < i; j++)
                if(matrixScheme[i][j].getUsedCell())
                    if(this.colour==matrixScheme[i][j].getDie().getColour())
                        score += matrixScheme[i][j].getDie().getNumber();

        return score;
    }
}
