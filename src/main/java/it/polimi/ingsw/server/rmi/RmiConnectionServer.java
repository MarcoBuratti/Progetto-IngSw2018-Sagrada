package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.util.Message;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.StringTokenizer;

public class RmiConnectionServer extends Observable implements RmiServerInterface, ServerInterface {

    private Server server;
    private Game game;
    private Player player;
    private RmiClientInterface client;
    private boolean gameStarted = false;
    private String defaultScheme;

    /**
     * Creates a new server thread associated with the rmi client connection.
     * @param connectionClientRMI the reference to the client connection
     * @param server the server
     */
    RmiConnectionServer(RmiClientInterface connectionClientRMI, Server server) {
        this.client = connectionClientRMI;
        this.server = server;
    }

    /**
     * Locks the thread waiting for the attribute gameStarted to be set as true.
     */
    private synchronized void waitGameStart() {
        while (!gameStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the schemes to the player.
     * @param schemes a String containing the extracted schemes
     */
    private synchronized void askForChosenScheme(String schemes) {
        this.send("schemes. " + schemes);
    }

    /**
     * Sets a default scheme before sending to the player the extracted scheme in order to set a scheme
     * in case the player makes his choice too late.
     * @param schemes the String containing the schemes
     * @return the name of the default scheme
     */
    private String defaultScheme(String schemes) {
        StringTokenizer strtok = new StringTokenizer(schemes, ",");
        String defaultScheme = strtok.nextToken();
        try {
            this.player.setDashboard(defaultScheme);
        } catch (NotValidValueException e) {
            e.printStackTrace();
        }
        return defaultScheme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerAndAskScheme(Message message) throws RemoteException {

        this.player = new Player( message.getMessage());
        boolean firstLog = !server.alreadyLoggedIn(this);

        if ( firstLog ) {
            server.registerConnection(this);
            waitGameStart();
            String schemes = game.selectSchemes();
            this.defaultScheme = defaultScheme(schemes);
            Color privateAchievementColor = game.selectPrivateAchievement();
            this.player.setPrivateAchievement(new PrivateAchievement(privateAchievementColor));;
            this.send("Your private achievement is: " + privateAchievementColor);
            askForChosenScheme(schemes);
        }
        else server.registerConnection(this);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setDashboard(Message message) throws RemoteException {
        try {
            boolean schemeChosen = game.isSchemeChosen();
            if (!schemeChosen) {
                this.player.setDashboard(message.getMessage());
                this.send("You have chosen the following scheme: " + message.getMessage() + "\n" + this.player.getDashboard().toString() + "\nPlease wait, the game will start soon.");
            } else {
                this.send("Too late! Your scheme is: " + defaultScheme + "\n" + this.player.getDashboard().toString() + "\nThe game has already started!");
            }
        } catch (NotValidValueException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMove(PlayerMove playerMove) throws RemoteException {
        send("Trying to make the move ...");
        setChanged();
        notifyObservers(playerMove);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() throws RemoteException {
        this.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Player getPlayer() {
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void send(String string) {
        try {
            client.update(string);
        } catch (Exception e) {
            server.deregisterConnection(this);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        send("ConnectionClient expired.");
        send("Terminate.");
        server.deregisterConnection(this);
        send("You've been disconnected successfully.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void update(Observable o, Object arg) {
        gameStarted = true;
        notifyAll();
    }
}