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

    public boolean toolEffect ( Turn turn, PlayerMove playerMove ) {

        if ( !turn.isSecondTurn() && turn.isPlacementDone() && playerMove.getIndexDie().isPresent() ) {

            if ( playerMove.getIndexDie().get() >= turn.getGameBoard().getDraftPool().size() )
                return false;

            turn.setPlacementDone( false );
            turn.tryPlacementMove( playerMove );

            if( turn.isPlacementDone() ) {

                turn.getPlayer().setSkipSecondTurn( true );
                return true;

            }

            else {
                turn.setPlacementDone( true );
                return false;
            }
        }
        return false;
    }

    public Color getColor() {
        return this.toolName.getColor();
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }

    public ToolNames getToolName() {
        return toolName;
    }
}