package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class ChangeDieTool implements Tool {

    private boolean isAlreadyUsed;
    private Color color;
    private boolean needPlacement;
    private ToolNames toolName;

    private Die secondDie;
    private Die dieDraftPool;
    private int index;

    public ChangeDieTool(boolean needPlacement, ToolNames toolName) {
        this.color = toolName.getColor();
        this.toolName = toolName;
        this.needPlacement = needPlacement;
        this.isAlreadyUsed = false;

    }


    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;

    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if (playerMove.getIndexDie().isPresent()) {
            if (toolName.equals(ToolNames.LENS_CUTTER)) {
                try {
                    secondDie = (Die) turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).get(playerMove.getIntParameters(1));
                    dieDraftPool = turn.getGameBoard().changeDie(secondDie, playerMove.getIndexDie().get());
                    turn.getGameBoard().getRoundTrack().changeDie(dieDraftPool, playerMove.getIntParameters(0), playerMove.getIntParameters(1));
                    return true;
                } catch (NotValidRoundException e) {
                    e.printStackTrace();
                }
            } else if (toolName.equals(ToolNames.FLUX_REMOVER)) {
                dieDraftPool = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie().get());
                secondDie = turn.getGameBoard().getDiceBag().changeDie(dieDraftPool);
                turn.getGameBoard().changeDie(secondDie, playerMove.getIndexDie().get());
                index = playerMove.getIndexDie().get();

                return true;
            }

            return false;
        } else
            throw new IllegalArgumentException();

    }

    public Color getColor() {
        return this.color;
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }

    public void placementDie(Turn turn) {
        if (turn.getPlayerMove().getIndexDie().isPresent()) {
            if (turn.getTypeMove().equals("PlaceDie") && !turn.isPlacementDone() && (index == turn.getPlayerMove().getIndexDie().get())) {
                if (toolName.equals(ToolNames.FLUX_REMOVER)) {
                    try {
                        turn.getGameBoard().getDraftPool().get(index).setNumber(turn.getPlayerMove().getIntParameters(2));
                    } catch (NotValidValueException e) {
                        e.printStackTrace();
                    }
                }
                turn.tryPlacementMove(turn.getPlayerMove());

            } else
                turn.setWaitMove(true);
        } else
            throw new IllegalArgumentException();
    }

    public ToolNames getToolName() {
        return this.toolName;
    }

}
