package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.EndedGameException;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;

import java.util.Optional;

public class ChangeDieTool implements Tool {

    private boolean isAlreadyUsed;
    private boolean needPlacement;
    private ToolNames toolName;

    /**
     * Creates a ChangeDieTool, a class used to manage the following tools: LENS CUTTER, FLUX REMOVER (using decorator DecoratedChangeDieTool).
     *
     * @param needPlacement a boolean which specifies whether the tool has already been used once or not
     * @param toolName      an instance of ToolNames enum representing the tool's name
     */
    ChangeDieTool(boolean needPlacement, ToolNames toolName) {
        this.toolName = toolName;
        this.needPlacement = needPlacement;
        this.isAlreadyUsed = false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed = alreadyUsed;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean toolEffect(Turn turn, PlayerMove playerMove) {

        Optional<Integer> dieIndex = playerMove.getIndexDie();

        if (dieIndex.isPresent()) {

            Integer dieIndexValue = dieIndex.get();

            if (dieIndexValue < turn.getGameBoard().getDraftPool().size() && toolName.equals(ToolNames.LENS_CUTTER)) {
                try {
                    if (playerMove.getIntParameters(0) < turn.getGameBoard().getRoundTrack().getCurrentRound()
                            && playerMove.getIntParameters(1) < turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).size()) {
                        Die secondDie = turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).get(playerMove.getIntParameters(1));
                        Die dieDraftPool = turn.getGameBoard().changeDie(secondDie, dieIndexValue);
                        turn.getGameBoard().getRoundTrack().changeDie(dieDraftPool, playerMove.getIntParameters(0), playerMove.getIntParameters(1));
                        return true;
                    }
                } catch (NotValidRoundException e) {
                    return false;
                } catch (EndedGameException e) {
                    e.printStackTrace();
                }
            }
            return false;
        } else
            throw new IllegalArgumentException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needPlacement() {
        return this.needPlacement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolNames getToolName() {
        return this.toolName;
    }

}
