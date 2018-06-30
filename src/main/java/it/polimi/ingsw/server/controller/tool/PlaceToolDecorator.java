package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

public abstract class PlaceToolDecorator implements Tool {

    public boolean placeDie (Turn turn, PlayerMove playerMove) {

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

}
