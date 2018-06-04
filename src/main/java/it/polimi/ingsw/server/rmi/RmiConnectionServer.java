package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.util.Message;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.StringTokenizer;

public class RmiConnectionServer extends Observable implements RmiServerInterface, ServerInterface {

    Server server;
    private Player player;
    private RmiClientInterface client;
    private boolean gameStarted;
    private boolean firstLog = true;
    private String defaultScheme;


    public RmiConnectionServer(RmiClientInterface connectionClientRMI, Server server) {
        this.client = connectionClientRMI;
        this.server = server;
    }

    @Override
    public synchronized void setPlayerAndAskScheme(Message message) throws RemoteException {
        this.setPlayer(new Player(message.getMessage(), this));
        this.gameStarted = server.isGameStarted();
        this.firstLog = !server.alreadyLoggedIn(this);
        try {
            if(firstLog && !gameStarted) {
                String schemes = server.selectSchemes();
                this.defaultScheme = defaultScheme(schemes);
                Color privateAchievementColor = server.selectPrivateAchievement();
                this.player.setPrivateAchievement(new PrivateAchievement(privateAchievementColor));
                server.registerConnection(this);
                this.send("Your private achievement is: " + privateAchievementColor);
                askForChosenScheme(schemes);
            }
            else
                server.registerConnection(this);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public synchronized void setDashboard(Message message) throws RemoteException {
        try {
            gameStarted = server.isGameStarted();
            if (!gameStarted) {
                this.player.setDashboard(message.getMessage());
                this.send("You have chosen the following scheme: " + message.getMessage() + "\n" + this.player.getDashboard().toString() + "\nPlease wait, the game will start soon.");
            }
            else {
                this.send("Too late! Your scheme is: " + defaultScheme + "\n" + this.player.getDashboard().toString() + "\nThe game has already started!");
            }
        } catch (NotValidValueException e) {
            System.err.println(e.toString());
        }
    }

    @Override
    public void sendMove(PlayerMove playerMove) throws RemoteException {
        send("Trying to make the following move: " + playerMove.toString() + " ...");
        setChanged();
        notifyObservers(playerMove);
    }

    @Override
    public void quit() throws RemoteException {
        this.close();
    }

    @Override
    public synchronized void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }


    @Override
    public synchronized void send(String string) {
        try {
            client.update(string);
        } catch (Exception e) {
            server.deregisterConnection(this);
        }

    }

    @Override
    public void close() {
        send("Connection expired.");
        send("Terminate.");
        server.deregisterConnection(this);
        send("You've been disconnected successfully.");
    }

    public synchronized void askForChosenScheme(String schemes) throws IOException {
        this.send("schemes. " + schemes);
    }


    private String defaultScheme (String schemes) {
        StringTokenizer strtok = new StringTokenizer(schemes, ",");
        String defaultScheme = strtok.nextToken();
        try {
            this.player.setDashboard(defaultScheme);
        } catch (NotValidValueException e) {
            e.printStackTrace();
        }
        return defaultScheme;
    }

}