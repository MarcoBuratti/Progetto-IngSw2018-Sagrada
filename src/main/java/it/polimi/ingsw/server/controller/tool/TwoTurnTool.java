package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;

public class TwoTurnTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    public TwoTurnTool(boolean needPlacement, ToolNames toolName) {
        this.needPlacement = needPlacement;
        this.toolName = toolName;
        this.isAlreadyUsed = false;
    }

    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;
    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if (!turn.isSecondTurn() && turn.isPlacementDone()) {
            turn.setPlacementDone(false);
            turn.getPlayer().setSkipSecondTurn(true);
            return true;
        }
        return false;
    }

    public Color getColor() {
        return this.toolName.getColor();
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }

    public void placementDie(Turn turn) {
    }

    public ToolNames getToolName() {
        return toolName;
    }
}