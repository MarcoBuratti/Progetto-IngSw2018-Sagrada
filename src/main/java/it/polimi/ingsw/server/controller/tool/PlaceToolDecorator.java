package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;

public abstract class PlaceToolDecorator implements Tool {

    /**
     * Allows the user to use a placement move during the use of a tool and returns true if the placement move has been done successfully.
     *
     * @param turn       the turn being played
     * @param playerMove the PlayerMove Object representing the move
     * @return a boolean specifying whether the die has been placed successfully
     */
    public abstract boolean placeDie(Turn turn, PlayerMove playerMove);

    /**
     * Checks if the die has been placed on the selected cell and communicates it to the player.
     *
     * @param turn          the turn being played
     * @param placementMove the PlacementMove Object representing the move
     * @return a boolean specifying whether the die has been placed successfully
     */
    boolean placeDieCheck(Turn turn, PlacementMove placementMove) {
        if (turn.isPlacementDone()) {
            turn.getGameBoard().removeDieFromDraftPool(placementMove.getDie());
            turn.sendToPlayer("The die has been placed on the selected cell.");
            return true;
        } else {
            turn.sendToPlayer("Incorrect move! Please try again.");
            return false;
        }
    }

    /**
     * Sends to the player a message communicating that it's impossible to place the die anywhere in the matrix.
     *
     * @param turn the turn being played
     */
    void unableToPlaceDie(Turn turn) {
        turn.sendToPlayer("You cannot place this die anyway!");
    }

    /**
     * Returns a boolean which specifies if it's impossible to place the selected die anywhere.
     *
     * @param turn the turn being played
     * @param die  the die the player wants to place
     * @return a boolean
     */
    protected abstract boolean cantPlaceDie(Turn turn, Die die);

}
