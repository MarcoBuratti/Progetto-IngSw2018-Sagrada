package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;

public class DecoratedSetDieTool extends PlaceToolDecorator {
    private Tool myTool;

    public DecoratedSetDieTool ( Tool tool ) {
        myTool = tool;
    }

    @Override
    public boolean isAlreadyUsed() {
        return myTool.isAlreadyUsed();
    }

    @Override
    public void setAlreadyUsed(boolean alreadyUsed) {
        myTool.setAlreadyUsed(alreadyUsed);
    }

    @Override
    public boolean toolEffect(Turn turn, PlayerMove playerMove) {

        if ( playerMove.getIndexDie().isPresent() ) {

            if (playerMove.getIndexDie().get() >= turn.getGameBoard().getDraftPool().size()) {
                return false;
            }

            if ( myTool.getToolName().equals( ToolNames.FLUX_BRUSH ) ) {
                turn.getGameBoard().getDraftPool().get( playerMove.getIndexDie().get() ).extractAgain();
                turn.getGameBoard().update();
                return true;
            }

            else return false;

        }

        else return false;

    }

    @Override
    public Color getColor() {
        return myTool.getColor();
    }

    @Override
    public boolean needPlacement() {
        return myTool.needPlacement();
    }

    @Override
    public ToolNames getToolName() {
        return myTool.getToolName();
    }
}
