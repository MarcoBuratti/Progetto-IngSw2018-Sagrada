package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.interfaces.*;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.util.Message;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.StringTokenizer;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class RmiConnectionServer extends Observable implements RmiServerInterface, ServerInterface {

    Server server;
    private Player player;
    private RmiClientInterface client;
    private boolean gameStarted;
    private boolean firstLog = true;


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
                defaultScheme(schemes);
                server.registerConnection(this);
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
            this.player.setDashboard(message.getMessage());
            send("You have chosen the following scheme: " + message.getMessage() + "\n" + this.player.getDashboard().toString() + "\nPlease wait, the game will start soon.");

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
    }

    public synchronized void askForChosenScheme(String schemes) throws IOException {
        this.send("Please choose one of these schemes: insert a number between 1 and 4. " + schemes);
    }


    private void defaultScheme (String schemes) {
        StringTokenizer strtok = new StringTokenizer(schemes, ",");
        try {
            this.player.setDashboard(strtok.nextToken());
        } catch (NotValidValueException e) {
            e.printStackTrace();
        }
    }

}