package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class RemoteView extends Observable implements Observer {

    private Player player;
    private Game game;
    private ServerInterface serverInterface;
    private MessageReceiver messageReceiver;
    private boolean gameStarted;

    public RemoteView(ServerInterface serverInterface) {
        gameStarted = false;
        this.serverInterface = serverInterface;
        player = serverInterface.getPlayer();
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    synchronized void changeConnection(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
        this.serverInterface.setGame(game);
        this.serverInterface.setPlayer(player);
        this.player.setServerInterface(this.serverInterface);
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    synchronized void setModelView ( ModelView modelView ) {
        modelView.addObserver(this);
    }

    Game getGame () { return this.game; }

    synchronized void setGame (Game game) {
        this.game = game;
        this.setGameStarted();
        if ( this.serverInterface != null )
            this.serverInterface.setGame(game);
    }

    synchronized void removeConnection() {
        this.serverInterface = null;
    }

    public Player getPlayer() {
        return player;
    }

    ServerInterface getServerInterface() {
        return serverInterface;
    }

    void showGameBoard(ModelView modelView) {
        if (serverInterface != null) {
            ArrayList<Player> players = modelView.getModel().getPlayers();
            serverInterface.send(modelView.getModel().sendTool());
            serverInterface.send(modelView.getModel().sendAchievement());
            serverInterface.send(modelView.getModel().sendRoundTrack());
            serverInterface.send(modelView.getModel().sendDraft());
            for (Player p : players)
                serverInterface.send(p.getDashboard().toString());
        }
    }

    public void send(String string) {
        serverInterface.send(string);
    }

    public void notYourTurn() {
        serverInterface.send("It's not your turn. Please wait.");
    }

    public void incorrectMove () { serverInterface.send("Your move is incorrect. Arguments not allowed.");}

    private void process(PlayerMove playerMove){
        setChanged();
        notifyObservers(playerMove);
    }

    synchronized boolean isGameStarted () {
        return this.gameStarted;
    }

    private synchronized void setGameStarted () {
        this.gameStarted = true;
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
        showGameBoard((ModelView) o);
    }
}
