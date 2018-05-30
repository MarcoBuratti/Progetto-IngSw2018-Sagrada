package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.PlacementCheck;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class SetDieTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    private Die die;

    public SetDieTool(boolean needPlacement, ToolNames toolName) {
        this.toolName = toolName;
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

        if(playerMove.getIndexDie().isPresent()) {

            if (toolName.equals(ToolNames.FLUX_BRUSH) && turn.isPlacementDone()) {
                return false;
            }
            die = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie().get());
            int oldValue = die.getNumber();
            try {
                if (toolName.equals(ToolNames.GROZING_PLIERS)&&playerMove.getAddOne().isPresent()) {
                    if (playerMove.getAddOne().get())
                        die.setNumber(oldValue + 1);
                    else
                        die.setNumber(oldValue - 1);
                    return true;
                } else if (toolName.equals(ToolNames.GRINDING_STONE)) {
                    die.setNumber(7 - oldValue);
                    return true;
                } else if (toolName.equals(ToolNames.FLUX_BRUSH)) {
                    die.extractAgain();
                    return true;
                }
            } catch (NotValidValueException e) {
                System.out.println(e.toString());
            }
            return false;
        }else
            throw new IllegalArgumentException();
    }

    public boolean canPlace(Turn turn) {
        boolean canPlace = false;
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
        if(turn.getPlayerMove().getIndexDie().isPresent()) {
            if (turn.getTypeMove().equals("PlaceDie") && !turn.isPlacementDone() &&
                    die.equals(turn.getGameBoard().getDraftPool().get(turn.getPlayerMove().getIndexDie().get()))) {
                turn.tryPlacementMove(turn.getPlayerMove());
            } else
                turn.setWaitMove(true);
        }else
            throw new IllegalArgumentException();
    }

    public ToolNames getToolName() {
        return this.toolName;
    }

}

