package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.util.Message;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends Observable implements Observer {

    private Player player;
    private ServerInterface serverInterface;
    private MessageReceiver messageReceiver;

    public RemoteView(ServerInterface serverInterface, ModelView modelView){
        this.serverInterface = serverInterface;
        player = serverInterface.getPlayer();
        messageReceiver = new MessageReceiver();
        this.serverInterface.getMessageSender().addObserver(this.messageReceiver);
        modelView.addObserver(this);
    }

    public synchronized void ChangeConnection(ServerInterface serverInterface){
        this.serverInterface = serverInterface;
        this.serverInterface.setPlayer(player);
        this.player.setServerInterface(this.serverInterface);
        messageReceiver = new MessageReceiver();
        this.serverInterface.getMessageSender().addObserver(this.messageReceiver);
    }

    public Player getPlayer () {
        return player;
    }

    @Override
    public void update(Observable o, Object arg) {
        showGameboard((ModelView) o);
    }

    public void showGameboard (ModelView modelView){
        serverInterface.send(modelView.toString());
    }

    public void process(PlayerMove playerMove){
        setChanged();
        notifyObservers(playerMove);
    }

    private class MessageReceiver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            PlayerMove playerMove = (PlayerMove) arg;
            process(playerMove);

        }

    }
}
