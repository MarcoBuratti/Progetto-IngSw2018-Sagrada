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
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
        modelView.addObserver(this);
    }

    synchronized void changeConnection (ServerInterface serverInterface){
        this.serverInterface = serverInterface;
        this.serverInterface.setPlayer(player);
        this.player.setServerInterface(this.serverInterface);
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    public Player getPlayer () {
        return player;
    }


    private void showGameboard (ModelView modelView){
        serverInterface.send(modelView.toString());
    }

    public void send (String string) {
        serverInterface.send(string);
    }

    public void notYourTurn () {
        serverInterface.send("It's not your turn. Please wait.");
    }

    private void process(PlayerMove playerMove){
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

    @Override
    public void update(Observable o, Object arg) {
        showGameboard((ModelView) o);
    }
}
