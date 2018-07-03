package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;

public class PrivateAchievement implements CardAchievement {

    private Color color;

    /**
     * Creates a PrivateAchievement Object representing a private achievement.
     * Private achievement are associated with one color.
     * @param color the Color Object representing the color associated with the private achievement
     */
    public PrivateAchievement(Color color) {
        this.color = color;
    }

    /**
     * Returns an int representing the score effect associated with the player's Private Achievement.
     * The return value is the number of dice having the same color as the private achievement.
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing a score effect
     */
    @Override
    public int scoreEffect(Dashboard dashboard) {

        int score = 0;
        Cell[][] matrixScheme = dashboard.getMatrixScheme();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell() && this.color == matrixScheme[i][j].getDie().getColor())
                    score += matrixScheme[i][j].getDie().getNumber();

        return score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Your private achievement is: " + this.color.toString();
    }
}
