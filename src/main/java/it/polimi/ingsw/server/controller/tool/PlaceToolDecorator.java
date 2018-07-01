package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;

public abstract class PlaceToolDecorator implements Tool {

    public abstract boolean placeDie (Turn turn, PlayerMove playerMove);

    boolean placeDieCheck (Turn turn, PlacementMove placementMove) {
        if ( turn.isPlacementDone() ) {
            turn.getGameBoard().removeDieFromDraftPool(placementMove.getDie());
            turn.sendToPlayer("The die has been placed on the selected cell.");
            return true;
        } else {
            turn.sendToPlayer("Incorrect move! Please try again.");
            return false;
        }
    }

    void unableToPlaceDie(Turn turn) {
        turn.sendToPlayer("You cannot place this die anyway!");
    }

    protected abstract boolean canPlaceDie (Turn turn, Die die);

}
