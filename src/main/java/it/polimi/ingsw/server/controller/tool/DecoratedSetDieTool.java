package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlacementMove;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

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

            Die myDie = turn.getGameBoard().getDraftPool().get( playerMove.getIndexDie().get() );

            if ( myTool.getToolName().equals( ToolNames.FLUX_BRUSH ) ) {
                myDie.extractAgain();
                turn.getGameBoard().update();
                if ( !canPlaceDie( turn, myDie))
                    unableToPlaceDie( turn );
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

    @Override
    public boolean placeDie(Turn turn, PlayerMove playerMove) {

        if (playerMove.getIndexDie().isPresent()) {
            try {
                Die myDie = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie().get());

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

    @Override
    protected boolean canPlaceDie(Turn turn, Die die) {
        boolean canPlace = false;
        PlacementCheck placementCheck = new PlacementCheck();
        for (int i = 0; i < 4 && !canPlace; i++) {
            for (int j = 0; j < 5 && !canPlace; j++) {
                canPlace = placementCheck.genericCheck(i, j, die, turn.getPlayer().getDashboard().getMatrixScheme());
            }
        }
        return canPlace;
    }

}
