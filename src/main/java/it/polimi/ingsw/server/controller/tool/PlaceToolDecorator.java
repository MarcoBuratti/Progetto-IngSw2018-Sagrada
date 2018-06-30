package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;

public interface PlaceToolDecorator extends Tool {
    boolean placeDie (Turn turn, PlayerMove playerMove);
}
