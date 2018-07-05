package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;


import java.util.Optional;

public class TwoTurnTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    /**
     * Creates a ChangeDieTool, a class used to manage the following tools: RUNNING PLIERS.
     * @param needPlacement a boolean which specifies whether the tool has already been used once or not
     * @param toolName an instance of ToolNames enum representing the tool's name
     */
    TwoTurnTool(boolean needPlacement, ToolNames toolName) {
        this.needPlacement = needPlacement;
        this.toolName = toolName;
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
    public boolean toolEffect ( Turn turn, PlayerMove playerMove ) {

        Optional<Integer> dieIndex = playerMove.getIndexDie();

        if ( !turn.isSecondTurn() && turn.isPlacementDone() && dieIndex.isPresent() ) {

            Integer dieIndexValue = dieIndex.get();

            if ( dieIndexValue >= turn.getGameBoard().getDraftPool().size() )
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
        return toolName;
    }
}