package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;

public interface Tool {

    /**
     * Returns a boolean which specifies whether the tool has already been used once or not.
     *
     * @return a boolean
     */
    boolean isAlreadyUsed();

    /**
     * Allows the user to set the boolean which specifies whether the tool has already been used once or not.
     *
     * @param alreadyUsed the value of the boolean the user wants to set
     */
    void setAlreadyUsed(boolean alreadyUsed);

    /**
     * Returns a boolean which specifies whether the tool has been used successfully or not.
     *
     * @param turn       a Turn Object representing the turn
     * @param playerMove a PlayerMove Object representing the move
     * @return a boolean
     */
    boolean toolEffect(Turn turn, PlayerMove playerMove);

    /**
     * Returns a boolean specifying whether the tool needs a placement move or not.
     *
     * @return a boolean
     */
    boolean needPlacement();

    /**
     * Returns the name of the selected tool.
     *
     * @return an instance of the ToolNames Enum specifying the name of the selected tool
     */
    ToolNames getToolName();

}
