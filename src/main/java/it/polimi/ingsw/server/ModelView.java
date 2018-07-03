package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;

import java.util.Observable;
import java.util.Observer;

public class ModelView extends Observable implements Observer {

    private GameBoard model;

    /**
     * Creates a ModelView Object which allows the user to send updates of the model to the players.
     * @param gameBoard the model
     */
    ModelView(GameBoard gameBoard) {
        this.model = gameBoard;
        this.model.addObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable o, Object arg) {
        this.model = (GameBoard) o;
        setChanged();
        notifyObservers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        bld.append("*").append(model.sendTool());
        bld.append(model.sendAchievement());
        bld.append(model.sendRoundTrack());
        bld.append(model.sendDraft());
        int i = 0;
        for (Player p : model.getPlayers()) {
            bld.append("scheme").append(i);
            bld.append("-");
            bld.append(p.toString());
            bld.append("-");
            i++;
        }
        bld.append("numberPlayer" + "-");
        bld.append(i);
        return bld.toString();
    }


}
