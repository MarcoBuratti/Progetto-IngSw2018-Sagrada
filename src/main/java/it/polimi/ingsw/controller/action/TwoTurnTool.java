package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;

public class TwoTurnTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    public TwoTurnTool(boolean needPlacement,ToolNames toolName){
        this.needPlacement=needPlacement;
        this.toolName=toolName;
        this.isAlreadyUsed=false;
    }

    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed=alreadyUsed;
    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if(!turn.isSecondTurn()&&turn.isPlacementDone()){
            turn.setPlacementDone(false);
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
}