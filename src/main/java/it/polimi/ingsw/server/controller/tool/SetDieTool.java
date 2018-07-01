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

        if ( playerMove.getIndexDie().isPresent() ) {

            if ( playerMove.getIndexDie().get() >= turn.getGameBoard().getDraftPool().size() ) {
                return false;
            }

            Die die = turn.getGameBoard().getDraftPool().get( playerMove.getIndexDie().get() );


            int oldValue = die.getNumber();
            try {
                if ( toolName.equals( ToolNames.GROZING_PLIERS ) && playerMove.getAddOne().isPresent() ) {
                    if (playerMove.getAddOne().get())
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


    @Override
    public Color getColor() {
        return this.toolName.getColor();
    }

    public boolean needPlacement() {
        return needPlacement;
    }

    public ToolNames getToolName() {
        return this.toolName;
    }

}

