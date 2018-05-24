package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

public interface CardAchievement {
    int scoreEffect(Dashboard dashboard) throws OccupiedCellException;
}
