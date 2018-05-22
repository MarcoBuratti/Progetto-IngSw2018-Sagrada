package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.PlacementCheck;
import it.polimi.ingsw.model.exception.NotValidValueException;

public class ToolSetDie implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    private Die die;

    public ToolSetDie(boolean needPlacement, ToolNames toolName) {
        this.toolName=toolName;
        this.needPlacement = needPlacement;
        this.isAlreadyUsed = false;

    }

    @Override
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    @Override
    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;


    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if (!turn.isPlacementDone()) {
            die = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
            int oldValue = die.getNumber();
            try {
                if (playerMove.getToolName().equals(ToolNames.GROZING_PLIERS)) {
                    if (playerMove.getAddOne())
                        die.setNumber(oldValue + 1);
                    else
                        die.setNumber(oldValue - 1);
                    return true;
                } else if (playerMove.getToolName().equals(ToolNames.GRINDING_STONE)) {
                    die.setNumber(7-oldValue);
                    return true;
                } else if (playerMove.getToolName().equals(ToolNames.FLUX_BRUSH)) {
                    die.extractAgain();
                    return true;
                }
            } catch (NotValidValueException e)

            {
                e.printStackTrace();
            }
        }
            return false;
    }

    public boolean canPlace(Turn turn){
            boolean canPlace=false;
            PlacementCheck placementCheck = new PlacementCheck();
            for (int i = 0; i < 4 && !canPlace; i++) {
                for (int j = 0; j < 5 && !canPlace; j++) {
                        canPlace = placementCheck.genericCheck(i, j, die, turn.getPlayer().getDashboard().getMatrixScheme());
                    }
                }
            return canPlace;
    }

    @Override
    public Color getColor() {
        return this.toolName.getColor();
    }

    public boolean needPlacement() {
        return needPlacement;
    }

    public void placementDie(Turn turn) {
        if (turn.getTypeMove().equals("PlaceDie") && !turn.isPlacementDone() &&
                die.equals(turn.getGameBoard().getDraftPool().get(turn.getPlayerMove().getIndexDie()))) {
            turn.setMove(turn.getPlayerMove());
        }
    }

}

