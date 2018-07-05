package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

import java.util.Optional;

public class DecoratedSetDieTool extends PlaceToolDecorator {
    private Tool myTool;

    /**
     * Creates a DecoratedSetDieTool, a class used to manage the FLUX BRUSH tool. (Using the pattern decorator)
     * @param tool the Tool Object representing the selected tool
     */
    public DecoratedSetDieTool ( Tool tool ) {
        myTool = tool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlreadyUsed() {
        return myTool.isAlreadyUsed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlreadyUsed(boolean alreadyUsed) {
        myTool.setAlreadyUsed(alreadyUsed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean toolEffect(Turn turn, PlayerMove playerMove) {

        Optional<Integer> dieIndex = playerMove.getIndexDie();

        if (dieIndex.isPresent()) {

            Integer dieIndexValue = dieIndex.get();

            if (dieIndexValue >= turn.getGameBoard().getDraftPool().size()) {
                return false;
            }

            Die myDie = turn.getGameBoard().getDraftPool().get( dieIndexValue );

            if ( myTool.getToolName().equals( ToolNames.FLUX_BRUSH ) ) {
                myDie.extractAgain();
                turn.getGameBoard().update();
                if (cantPlaceDie(turn, myDie))
                    unableToPlaceDie( turn );
                return true;
            }

            else return false;

        }

        else return false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needPlacement() {
        return myTool.needPlacement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolNames getToolName() {
        return myTool.getToolName();
    }

    /**
     * Allows the user to manage a placement move.
     * @param turn the turn being played
     * @param playerMove the PlayerMove Object representing the move
     * @return a boolean specifying whether the die has been placed successfully
     */
    @Override
    public boolean placeDie(Turn turn, PlayerMove playerMove) {

        Optional<Integer> dieIndex = playerMove.getIndexDie();

        if (dieIndex.isPresent()) {

            Integer dieIndexValue = dieIndex.get();
            try {
                Die myDie = turn.getGameBoard().getDraftPool().get(dieIndexValue);

                PlacementMove placementMove = new PlacementMove(turn.getPlayer(), playerMove.getIntParameters(0),
                        playerMove.getIntParameters(1), myDie);
                turn.setPlacementDone(placementMove.placeDie());
                return placeDieCheck(turn, placementMove);

            } catch (OccupiedCellException | NotValidParametersException e) {
                return false;
            }
        } else
            return false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean cantPlaceDie(Turn turn, Die die) {
        boolean canPlace = false;
        PlacementCheck placementCheck = new PlacementCheck();
        for (int i = 0; i < 4 && !canPlace; i++) {
            for (int j = 0; j < 5 && !canPlace; j++) {
                canPlace = placementCheck.genericCheck(i, j, die, turn.getPlayer().getDashboard().getMatrixScheme());
            }
        }
        return !canPlace;
    }

}
