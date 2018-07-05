package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;

public class RollAgainTool implements Tool {
    private boolean isAlreadyUsed;
    private boolean needPlacement;
    private ToolNames toolName;

    /**
     * Creates a ChangeDieTool, a class used to manage the following tools: GLAZING HAMMER.
     * @param needPlacement a boolean which specifies whether the tool has already been used once or not
     * @param toolName an instance of ToolNames enum representing the tool's name
     */
    RollAgainTool(boolean needPlacement, ToolNames toolName) {
        this.toolName = toolName;
        this.needPlacement = needPlacement;
        this.isAlreadyUsed = false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if (toolName.equals(ToolNames.GLAZING_HAMMER) && !turn.isPlacementDone() && turn.isSecondTurn()) {
            turn.getGameBoard().getDraftPool().forEach(Die::extractAgain);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needPlacement() {
        return this.needPlacement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolNames getToolName() {
        return this.toolName;
    }
}
