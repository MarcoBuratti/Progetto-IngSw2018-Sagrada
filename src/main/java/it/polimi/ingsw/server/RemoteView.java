package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends Observable implements Observer {

    private Player player;
    private Game game;
    private ServerInterface serverInterface;
    private MessageReceiver messageReceiver;
    private boolean gameStarted;

    /**
     * Creates a RemoteView Object.
     * A RemoteView has references to:
     * - player: the Player Object referring to the player
     * - game: the Game Object referring to the game the player is playing
     * - serverInterface: the ServerInterface Object belonging to the player ( connection )
     * - messageReceiver: a MessageReceiver Object which allows the RemoteView to receive PlayerMove Objects and send them to the Controller
     * - gameStarted: a boolean specifying whether the game has started or not
     *
     * @param serverInterface the ServerInterface Object belonging to the player
     */
    public RemoteView(ServerInterface serverInterface) {
        gameStarted = false;
        this.serverInterface = serverInterface;
        player = serverInterface.getPlayer();
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    /**
     * Returns a boolean specifying whether serverInterface is null or not.
     *
     * @return a boolean
     */
    public boolean isOn() {
        return (this.serverInterface != null);
    }

    /**
     * Change the references to the player's connection.
     *
     * @param serverInterface a ServerInterface Object representing the player's new connection
     */
    synchronized void changeConnection(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
        this.serverInterface.setGame(game);
        this.serverInterface.setPlayer(player);
        messageReceiver = new MessageReceiver();
        Observable serverConnection = (Observable) this.serverInterface;
        serverConnection.addObserver(this.messageReceiver);
    }

    /**
     * When the ModelView Object is created, this method allows the user to add the remote view to its observers.
     *
     * @param modelView the ModelView Object
     */
    synchronized void setModelView(ModelView modelView) {
        modelView.addObserver(this);
    }

    /**
     * Returns the game attribute.
     *
     * @return a Game Object
     */
    synchronized Game getGame() {
        return this.game;
    }

    /**
     * Allows the user to set the game attribute.
     *
     * @param game the Game Object the user wants to set as game attribute
     */
    synchronized void setGame(Game game) {
        this.game = game;
        this.setGameStarted();
        if (this.serverInterface != null)
            this.serverInterface.setGame(game);
    }

    /**
     * Allows the user to remove the connection reference, setting the serverInterface attribute as null.
     */
    synchronized void removeConnection() {
        this.serverInterface = null;
    }

    /**
     * Returns the player attribute.
     *
     * @return a Player Object representing the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the serverInterface attribute.
     *
     * @return a ServerInterface Object representing the player's connection
     */
    ServerInterface getServerInterface() {
        return serverInterface;
    }

    /**
     * Sends the player the model's representation.
     *
     * @param modelView the ModelView Object of which the method toString is called
     */
    void showGameBoard(ModelView modelView) {
        send(player.getPrivateAchievement().toString());
        send(modelView.toString());
    }

    /**
     * Sends a String to the player's connection.
     *
     * @param string the String the user wants to send
     */
    public void send(String string) {
        if (isOn())
            serverInterface.send(string);
    }

    /**
     * Sends the player a message telling him that it's not his turn.
     */
    public void notYourTurn() {
        serverInterface.send("It's not your turn. Please wait.");
    }

    /**
     * Sends the player a message telling him that the move he's tried to do is incorrect.
     */
    public void incorrectMove() {
        serverInterface.send("Your move is incorrect.");
    }

    /**
     * Returns the gameStarted attribute.
     *
     * @return a boolean
     */
    synchronized boolean isGameStarted() {
        return this.gameStarted;
    }

    /**
     * Allows the user to set the gameStarted attribute.
     */
    private synchronized void setGameStarted() {
        this.gameStarted = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable o, Object arg) {
        showGameBoard((ModelView) o);
    }

    /**
     * A nested class used to receive and send PlayerMove Objects (from ServerInterface to Controller)
     */
    private class MessageReceiver implements Observer {
        /**
         * {@inheritDoc}
         */
        @Override
        public void update(Observable o, Object arg) {
            PlayerMove playerMove = (PlayerMove) arg;
            process(playerMove);
        }

        /**
         * Notifies the observers that a new PlayerMove has arrived.
         *
         * @param playerMove the new PlayerMove received from the player
         */
        private void process(PlayerMove playerMove) {
            setChanged();
            notifyObservers(playerMove);
        }
    }
}
