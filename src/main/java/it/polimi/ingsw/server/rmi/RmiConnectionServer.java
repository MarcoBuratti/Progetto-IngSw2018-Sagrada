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

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class RmiConnectionServer extends Observable implements RmiServerInterface, ServerInterface {

    Server server;
    private Player player;
    private RmiClientInterface client;
    private boolean isYourTurn;
    private MessageSender messageSender;


    public RmiConnectionServer(RmiClientInterface connectionClientRMI, Server server) {
        this.client = connectionClientRMI;
        this.server = server;
        this.messageSender = new MessageSender();
    }

    @Override
    public void setPlayerAndAskScheme(Message message) throws RemoteException{
        this.setPlayer(new Player(message.getMessage(), this));
        server.registerConnection(this);
        try {
            askForChosenScheme();
        }catch (Exception e){
            System.err.println(e.toString());
        }
    }

    @Override
    public void setDashboard(Message message) throws RemoteException {
        try {
            System.out.println(message.getMessage());
            this.player.setDashboard(message.getMessage());
        } catch (NotValidValueException e) {
            System.err.println(e.toString());
        }
    }

    public void notYourTurn () { send("It's not your turn! Please wait.");}

    @Override
    public void sendMove(PlayerMove playerMove) throws RemoteException {
        if(getYourTurn()) {
            messageSender.sendMove(playerMove);
        } else notYourTurn();
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
    public synchronized boolean getYourTurn() {
        return isYourTurn;
    }

    @Override
    public synchronized void setYourTurn(boolean bool) {
        this.isYourTurn = bool;
    }

    @Override
    public void send(String string) {
        try{
            client.update(string);
        }catch(ConnectException e) {
            e.printStackTrace();
            System.out.println("Client rimosso! - 1");
            close();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Client rimosso! - 2");
            close();
        }


        //connectionClientRMI.
        //sendback to client
    }

    @Override
    public void close() {

        send("Connessione terminata!");
        send("Terminate");
        server.deregisterConnection(this);
    }

    private class MessageSender extends Observable {

        private void sendMove (PlayerMove playerMove){
            setChanged();
            notifyObservers(playerMove);
        }


    }

    @Override
    public Observable getMessageSender() {
        return messageSender;
    }

    public void askForChosenScheme() throws IOException {
        StringBuilder bld = new StringBuilder();
        bld.append(server.getSchemes().get(0).getFirstScheme() + "," + server.getSchemes().get(0).getSecondScheme());
        bld.append(",");
        server.getSchemes().remove(0);
        bld.append(server.getSchemes().get(0).getFirstScheme() + "," + server.getSchemes().get(0).getSecondScheme());
        String message = bld.toString();
        server.getSchemes().remove(0);
        this.send("Please choose one of these schemes in a minute: insert a number between 1 and 4.\n" + message);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean bool = (boolean) arg;
        this.setYourTurn(bool);
    }
}