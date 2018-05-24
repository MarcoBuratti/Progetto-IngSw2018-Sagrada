package it.polimi.ingsw.controller.tool;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.controller.action.PlayerMove;
import it.polimi.ingsw.model.Color;

public interface Tool {

    boolean isAlreadyUsed();

    void setAlreadyUsed(boolean alreadyUsed);

    boolean toolEffect(Turn turn, PlayerMove playerMove);

    Color getColor();

    boolean needPlacement();

}
