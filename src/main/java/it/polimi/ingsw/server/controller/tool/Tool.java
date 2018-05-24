package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;

public interface Tool {

    boolean isAlreadyUsed();

    void setAlreadyUsed(boolean alreadyUsed);

    boolean toolEffect(Turn turn, PlayerMove playerMove);

    Color getColor();

    boolean needPlacement();

}
