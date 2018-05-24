package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;

public class RerollTool implements Tool {
    private boolean isAlreadyUsed;
    private Color color;
    private boolean needPlacement;
    private ToolNames toolName;


    public RerollTool(boolean needPlacement, ToolNames toolName) {
        this.toolName = toolName;
        this.color = toolName.getColor();
        this.needPlacement = needPlacement;
        this.isAlreadyUsed = false;

    }


    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;
    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if (toolName.equals(ToolNames.GLAZING_HAMMER)) {
            if (!turn.isPlacementDone() && turn.isSecondTurn()) {
                turn.getGameBoard().getDraftPool().forEach(Die::extractAgain);
                return true;
            }
        }

        return false;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }
}
