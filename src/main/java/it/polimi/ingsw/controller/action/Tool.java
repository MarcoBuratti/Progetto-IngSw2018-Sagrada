package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;

public interface Tool {

    boolean isAlreadyUsed();
    void setAlreadyUsed(boolean alreadyUsed);
    boolean toolEffect(Turn turn,PlayerMove playerMove);
    Color getColor();
    boolean needPlacement();

}
