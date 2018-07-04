package it.polimi.ingsw.server.controller.tool;

import it.polimi.ingsw.server.controller.Turn;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

import java.util.Optional;

public class SetDieTool implements Tool {

    private boolean isAlreadyUsed;
    private ToolNames toolName;
    private boolean needPlacement;

    /**
     * Creates a ChangeDieTool, a class used to manage the following tools: GRINDING STONE, GROZING PLIERS, FLUX BRUSH (using decorator DecoratedSetDieTool).
     * @param needPlacement a boolean which specifies whether the tool has already been used once or not
     * @param toolName an instance of ToolNames enum representing the tool's name
     */
    SetDieTool(boolean needPlacement, ToolNames toolName) {
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

        if ( dieIndex.isPresent() ) {

            Integer dieIndexValue = dieIndex.get();

            if ( dieIndexValue >= turn.getGameBoard().getDraftPool().size() ) {
                return false;
            }

            Die die = turn.getGameBoard().getDraftPool().get( dieIndexValue );


            int oldValue = die.getNumber();
            try {

                Optional<Boolean> increaseNumber = playerMove.getAddOne();

                if ( toolName.equals( ToolNames.GROZING_PLIERS ) && increaseNumber.isPresent() ) {

                    boolean increaseNumberValue = increaseNumber.get();

                    if (increaseNumberValue)
                        die.setNumber( oldValue + 1 );
                    else
                        die.setNumber( oldValue - 1 );
                    turn.getGameBoard().update();
                    return true;
                }

                else if (toolName.equals(ToolNames.GRINDING_STONE)) {
                    die.setNumber(7 - oldValue);
                    turn.getGameBoard().update();
                    return true;
                }

            } catch (NotValidValueException e) {
                System.out.println(e.toString());
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
        return needPlacement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ToolNames getToolName() {
        return this.toolName;
    }

}

