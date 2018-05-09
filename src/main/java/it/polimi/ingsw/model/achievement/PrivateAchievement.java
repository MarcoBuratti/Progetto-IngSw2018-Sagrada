package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public class PrivateAchievement implements CardAchievement {

    private Colour colour;

    public PrivateAchievement(Colour colour){
        this.colour= colour;
    }

    public int scoreEffect(Dashboard dashboard) throws OccupiedCellException {

        int score = 0;
        Cell[][]matrixScheme= dashboard.getMatrixScheme();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if(matrixScheme[i][j].getUsedCell())
                    if(this.colour==matrixScheme[i][j].getDie().getColour())
                        score += matrixScheme[i][j].getDie().getNumber();

        return score;
    }

    @Override
    public String toString() {
        return "This is a private achievement: Colour: " + this.colour;
    }
}
