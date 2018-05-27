package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends Observable implements Observer {

    private Player player;
    private ServerInterface serverInterface;

    public RemoteView(ServerInterface serverInterface, ModelView modelView){
        this.serverInterface = serverInterface;
        player = serverInterface.getPlayer();
        this.serverInterface.getMessageSender().addObserver(new MessageReceiver());
        modelView.addObserver(this);
    }

    public void ChangeConnection(ServerInterface serverInterface){
        this.serverInterface = serverInterface;
        this.serverInterface.getMessageSender().addObserver(new MessageReceiver());
        this.serverInterface.send("You've logged in as: " + player.getNickname());
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
