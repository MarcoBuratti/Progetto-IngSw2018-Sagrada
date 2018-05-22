package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.exception.NotValidRoundException;
import it.polimi.ingsw.model.exception.NotValidValueException;

public class ChangeDieTool implements Tool {

    private boolean isAlreadyUsed;
    private Color color;
    private boolean needPlacement;
    private ToolNames toolName;

    private Die secondDie;
    private Die dieDraftPool;

    public ChangeDieTool(boolean needPlacement,ToolNames toolName){
        this.color=toolName.getColor();
        this.toolName=toolName;
        this.needPlacement=needPlacement;
        this.isAlreadyUsed=false;

    }


    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.isAlreadyUsed=alreadyUsed;

    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
        if(toolName.equals("Lens Cutter")) {
            try {
                secondDie = (Die) turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).get(playerMove.getIntParameters(1));
                dieDraftPool =turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
                turn.getGameBoard().getDraftPool().remove(playerMove.getIndexDie());
                turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).remove(playerMove.getIntParameters(1));
                turn.getGameBoard().getDraftPool().add(secondDie);
                turn.getGameBoard().getRoundTrack().getDiceList(playerMove.getIntParameters(0)).add(dieDraftPool);
                return true;
            } catch (NotValidRoundException e) {
                e.printStackTrace();
            }
        }
        else if(toolName.equals("Flux Remover")){
            dieDraftPool =turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
            secondDie = turn.getGameBoard().getDiceBag().changeDie(dieDraftPool);
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


    public void placementDie(Turn turn){
        if (turn.getTypeMove().equals("PlaceDie") && !turn.isPlacementDone() &&
                dieDraftPool.equals(turn.getGameBoard().getDraftPool().get(turn.getPlayerMove().getIndexDie()))) {
            if(toolName.equals("Flux Remover")) {
                try {
                    dieDraftPool.setNumber(turn.getPlayerMove().getSetOnDie());
                } catch (NotValidValueException e) {
                    e.printStackTrace();
                }
            }
            turn.setMove(turn.getPlayerMove());

        }
    }

}
