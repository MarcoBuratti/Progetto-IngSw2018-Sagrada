package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.ServerAbstractClass;
import it.polimi.ingsw.server.model.Player;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends Observable implements Observer {

    private Player player;
    private ServerAbstractClass serverAbstractClass;

    public RemoteView(ServerAbstractClass serverAbstractClass, ModelView modelView){
        this.serverAbstractClass = serverAbstractClass;
        player = serverAbstractClass.getPlayer();
        (serverAbstractClass).addObserver(new MessageReceiver());
        modelView.addObserver(this);
    }

    public void ChangeConnection(ServerAbstractClass serverAbstractClass){
        this.serverAbstractClass = serverAbstractClass;
        (serverAbstractClass).addObserver(new MessageReceiver());

    }

    public Player getPlayer () {
        return player;
    }

    @Override
    public void update(Observable o, Object arg) {
        showGameboard((ModelView) o);
    }

    public void showGameboard (ModelView modelView){
        serverAbstractClass.send(modelView.toString() + "\nIt's " + modelView.getCurrentPlayer() + "'s turn.\n");
    }

    public void process(PlayerMove playerMove){
        setChanged();
        notifyObservers(playerMove);
    }

    private class MessageReceiver implements Observer {

        @Override
        public void update(Observable o, Object arg) {		//riceve messaggio dalla view lato client

            PlayerMove playerMove = (PlayerMove) arg;
            process(playerMove);

        }

    }
}
