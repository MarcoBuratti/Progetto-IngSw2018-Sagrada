package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.EndedGameException;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class ChangeDieTool implements Tool {

    private boolean isAlreadyUsed;
    private Color color;
    private boolean needPlacement;
    private ToolNames toolName;

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
        if ( playerMove.getIndexDie().isPresent() ) {
            if ( playerMove.getIndexDie().get() < turn.getGameBoard().getDraftPool().size() ) {
                if (toolName.equals(ToolNames.LENS_CUTTER)) {
                    try {
                        if ( playerMove.getIntParameters(0) < turn.getGameBoard().getRoundTrack().getCurrentRound()
                                && playerMove.getIntParameters(1) < turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).size() ) {
                            Die secondDie = (Die) turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).get(playerMove.getIntParameters(1));
                            Die dieDraftPool = turn.getGameBoard().changeDie(secondDie, playerMove.getIndexDie().get());
                            turn.getGameBoard().getRoundTrack().changeDie(dieDraftPool, playerMove.getIntParameters(0), playerMove.getIntParameters(1));
                            return true;
                        }
                    } catch (NotValidRoundException e) {
                        return false;
                    } catch (EndedGameException e) {
                        e.printStackTrace();
                    }
                }
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

    public ToolNames getToolName() {
        return this.toolName;
    }

}
