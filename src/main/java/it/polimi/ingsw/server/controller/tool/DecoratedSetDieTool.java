package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

public class DecoratedSetDieTool implements PlaceToolDecorator {
    private Tool myTool;

    public DecoratedSetDieTool ( Tool tool ) {
        myTool = tool;
    }

    public boolean placeDie(Turn turn, PlayerMove playerMove) {

        if (playerMove.getIndexDie().isPresent()) {
            try {
                PlacementMove placementMove = new PlacementMove(turn.getPlayer(), playerMove.getIntParameters(0),
                        playerMove.getIntParameters(1), turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie().get()));
                turn.setPlacementDone ( placementMove.placeDie() );
                if ( turn.isPlacementDone() ) {
                    turn.getGameBoard().removeDieFromDraftPool(placementMove.getDie());
                    turn.sendToPlayer("The die has been placed on the selected cell.");
                    return true;
                } else {
                    turn.sendToPlayer("Incorrect move! Please try again.");
                    return false;
                }
            } catch (OccupiedCellException | NotValidParametersException e) {
                return false;
            }
        } else
            return false;
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
