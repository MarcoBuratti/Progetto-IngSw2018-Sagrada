package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.PlacementCheck;
import it.polimi.ingsw.model.exception.NotValidValueException;

public class ToolSetDie implements Tool {

    private boolean isAlreadyUsed;
    private Color color;
    private boolean needPlacement;

    private int newValue;
    private int oldValue;
    private Die die;

    public ToolSetDie(boolean needPlacement) throws NotValidValueException {
        this.needPlacement=needPlacement;
        this.isAlreadyUsed = false;

    }

    @Override
    public boolean isAlreadyUsed() {
        return this.isAlreadyUsed;
    }

    @Override
    public void setAlreadyUsed() {
        this.isAlreadyUsed = true;


    }

    public boolean toolEffect(Turn turn, PlayerMove playerMove) {
            if (!turn.isPlacementDone()) {
                die = turn.getGameBoard().getDraftPool().get(playerMove.getIndexDie());
                oldValue = die.getNumber();
                newValue = playerMove.getSetOnDie();
                try {
                    die.setNumber(newValue);
                } catch (NotValidValueException e) {
                    e.printStackTrace();
                }
                PlacementCheck placementCheck = new PlacementCheck();
                boolean canPlace = false;
                for (int i = 0; i < 4 && !canPlace; i++) {
                    for (int j = 0; j < 5 && !canPlace; j++) {
                        canPlace = placementCheck.genericCheck(i, j, die, turn.getPlayer().getDashboard().getMatrixScheme());
                    }

                }
                if (!canPlace){
                    try {
                        die.setNumber(oldValue);
                    } catch (NotValidValueException e) {
                        e.printStackTrace();
                    }
            }return canPlace;
                }
                return false;
            }

    @Override
    public Color getColor() {
        return null;
    }

    public boolean needPlacement() {
        return needPlacement;
    }

    public void placementDie(Turn turn){
        if (turn.getTypeMove().equals("setdie") && !turn.isPlacementDone() &&
            die.equals(turn.getGameBoard().getDraftPool().get(turn.getPlayerMove().getIndexDie()))) {
            turn.setMove(turn.getPlayerMove());
            }
    }

    public void endTurn(Turn turn){
        try {
            die.setNumber(oldValue);
        } catch (NotValidValueException e) {
            e.printStackTrace();
        }
    }

}

