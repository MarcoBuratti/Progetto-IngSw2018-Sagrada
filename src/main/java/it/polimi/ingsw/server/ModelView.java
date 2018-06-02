package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.GameBoard;

import java.util.Observable;
import java.util.Observer;

public class ModelView extends Observable implements Observer {

    public GameBoard model;

    public void setModel(GameBoard model) {
        this.model = model;
    }

    public ModelView (GameBoard gameBoard){
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
        return model.toString();
    }


}
