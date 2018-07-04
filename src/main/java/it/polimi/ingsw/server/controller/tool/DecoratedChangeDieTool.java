package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

import java.util.Optional;

public class DecoratedChangeDieTool extends PlaceToolDecorator {

    private Tool myTool;

    /**
     * Creates a DecoratedChangeDieTool, a class used to manage the FLUX REMOVER tool. (Using the pattern decorator)
     * @param tool the Tool Object representing the selected tool
     */
    public DecoratedChangeDieTool ( Tool tool ) {
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

            if ( dieIndexValue >= turn.getGameBoard().getDraftPool().size() )
                return false;

            if ( myTool.getToolName().equals(ToolNames.FLUX_REMOVER )) {
                Die dieFromDraftPool = turn.getGameBoard().getDraftPool().get( dieIndexValue );
                Die newDie = turn.getGameBoard().getDiceBag().changeDie( dieFromDraftPool );
                turn.getGameBoard().changeDie( newDie, dieIndexValue );
                turn.getGameBoard().update();
                if (cantPlaceDie(turn, newDie))
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
     * Allows the user to manage a special placement move, where the player specifies the value he wants to set on the die.
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

                turn.getGameBoard().getDraftPool().get(dieIndexValue).setNumber(playerMove.getIntParameters(0));
                PlacementMove placementMove = new PlacementMove(turn.getPlayer(), playerMove.getIntParameters(1),
                        playerMove.getIntParameters(2), myDie);
                turn.setPlacementDone(placementMove.placeDie());
                return placeDieCheck(turn, placementMove);

            } catch (OccupiedCellException | NotValidParametersException | NotValidValueException e) {
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
        Die myDie = new Die ( die.getColor() );
        boolean canPlace = false;
        PlacementCheck placementCheck = new PlacementCheck();
        int myDieValue = 0;
        while ( myDieValue < 6 && !canPlace ) {
            myDieValue++;
            try {
                myDie.setNumber( myDieValue );
            } catch ( NotValidValueException e ) {
                e.printStackTrace();
            }
            for (int i = 0; i < 4 && !canPlace; i++) {
                for (int j = 0; j < 5 && !canPlace; j++) {
                    canPlace = placementCheck.genericCheck(i, j, die, turn.getPlayer().getDashboard().getMatrixScheme());
                }
            }
        }

        return !canPlace;

    }


}
