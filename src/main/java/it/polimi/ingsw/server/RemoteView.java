package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends Observable implements Observer {

    private Player player;
    private ServerInterface serverInterface;
    private MessageReceiver messageReceiver;

    public RemoteView(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
        player = serverInterface.getPlayer();
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    synchronized void changeConnection(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
        this.serverInterface.setPlayer(player);
        this.player.setServerInterface(this.serverInterface);
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    synchronized void setModelView(ModelView modelView) {
        modelView.addObserver(this);
    }

    synchronized void removeConnection() {
        this.serverInterface = null;
    }

    public Player getPlayer() {
        return player;
    }

    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    private void showGameboard(ModelView modelView) {
        if (serverInterface != null) {
            serverInterface.send(modelView.model.sendTool());
            serverInterface.send(modelView.model.sendAchievement());
            serverInterface.send(modelView.model.sendRoundTrack());
            serverInterface.send(modelView.model.sendDraft());
            serverInterface.send(modelView.model.toString());
        }
    }

    public void send(String string) {
        serverInterface.send(string);
    }

    public void notYourTurn() {
        serverInterface.send("It's not your turn. Please wait.");
    }

    private void process(PlayerMove playerMove) {
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
