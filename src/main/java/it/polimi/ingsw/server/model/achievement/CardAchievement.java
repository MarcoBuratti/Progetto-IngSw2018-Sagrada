package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Dashboard;

public interface CardAchievement {

    /**
     * Calculate the effect that the achievement has on the score of the player having the selected Dashboard.
     * @param dashboard the dashboard of the player whose score is being calculated
     * @return an int representing the score effect of the achievement
     */
    int scoreEffect(Dashboard dashboard);

}
