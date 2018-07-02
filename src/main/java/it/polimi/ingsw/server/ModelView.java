package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ModelView extends Observable implements Observer {

    private GameBoard model;

    public void setModel(GameBoard model) {
        this.model = model;
    }

    ModelView(GameBoard gameBoard) {
        this.model = gameBoard;
        this.model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.model = (GameBoard) o;
        setChanged();
        notifyObservers();
    }

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
