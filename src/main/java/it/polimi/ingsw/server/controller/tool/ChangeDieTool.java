package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

import java.util.ArrayList;

public class ChangeDieTool implements Tool {

    private boolean isAlreadyUsed;
    private Color color;
    private boolean needPlacement;
    private ToolNames toolName;

    private Die secondDie;
    private Die dieDraftPool;

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
        if (toolName.equals(ToolNames.LENS_CUTTER)) {
            try {
                secondDie = (Die) turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).get(playerMove.getIntParameters(1));
                dieDraftPool =turn.getGameBoard().changeDie(secondDie,playerMove.getIndexDie());
                turn.getGameBoard().getRoundTrack().changeDie(dieDraftPool,playerMove.getIntParameters(0),playerMove.getIntParameters(1));
                return true;
            } catch (NotValidRoundException e) {
                e.printStackTrace();
            }
        } else if (toolName.equals(ToolNames.FLUX_REMOVER)) {
            dieDraftPool = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
            secondDie = turn.getGameBoard().getDiceBag().changeDie(dieDraftPool);
            turn.getGameBoard().changeDie(secondDie,playerMove.getIndexDie());

            return true;
        }

        return false;

    }

    public Color getColor() {
        return this.color;
    }

    public boolean needPlacement() {
        return this.needPlacement;
    }

    public void placementDie(Turn turn) {
        if (turn.getTypeMove().equals("PlaceDie") && !turn.isPlacementDone() &&
                dieDraftPool.equals(turn.getGameBoard().getDraftPool().get(turn.getPlayerMove().getIndexDie()))) {
            if (toolName.equals("Flux Remover")) {
                try {
                    dieDraftPool.setNumber(turn.getPlayerMove().getSetOnDie());
                } catch (NotValidValueException e) {
                    e.printStackTrace();
                }
            }
            turn.setMove(turn.getPlayerMove());

        }
    }

    public ToolNames getToolName() {
        return this.toolName;
    }

}
