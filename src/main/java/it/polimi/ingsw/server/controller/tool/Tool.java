package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;

public interface Tool {

    /**
     * Returns a boolean which specifies whether the tool has already been used once or not.
     * @return a boolean
     */
    boolean isAlreadyUsed();

    /**
     * Allows the user to set the boolean which specifies whether the tool has already been used once or not.
     * @param alreadyUsed the value of the boolean the user wants to set
     */
    void setAlreadyUsed(boolean alreadyUsed);

    /**
     *
     * @param turn
     * @param playerMove
     * @return
     */
    boolean toolEffect(Turn turn, PlayerMove playerMove);

    Color getColor();

    boolean needPlacement();

    ToolNames getToolName();

}
