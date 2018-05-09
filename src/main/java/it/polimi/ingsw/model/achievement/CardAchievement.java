package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public interface CardAchievement {

    int scoreEffect(Dashboard dashboard) throws OccupiedCellException;
}
