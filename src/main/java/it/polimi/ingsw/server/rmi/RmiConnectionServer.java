package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.RmiServerInterface;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.ServerAbstractClass;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.util.Message;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Observable;

public class RmiConnectionServer extends ServerAbstractClass implements RmiServerInterface {

    Server server;
    boolean isOn = true;
    private Player player;
    private RmiClientInterface client;


    public RmiConnectionServer(RmiClientInterface connectionClientRMI, Server server) {
        this.client = connectionClientRMI;
        this.server = server;
        send("Attendere prego");
        server.registerConnection(this);
    }

    @Override
    public void setPlayer (Message message){
        this.setPlayer(new Player(message.getMessage(), this));
    }

    @Override
    public void setPlayer(Player player) {

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean getYourTurn() {
        return false;
    }

    @Override
    public void setYourTurn(boolean bool) {

    }

    @Override
    public void send(String message) {
        try{
            client.update(message);
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
        isOn = false;
        send("Connessione terminata!");
        send("Terminate");
        server.deregisterConnection(this);
    }

    public void receiveName (String name){
        setPlayer(new Player(name, this));
    }

    @Override
    public String askForChosenScheme() throws IOException {
        return null;
    }

    public void update(Message message) throws RemoteException{ //NOTIFICA LA REMOTE VIEW
        setChanged();
        notifyObservers(message);
    }

    @Override
    public void update(Observable o, Object arg) {
        //E' CHIAMATO DA TURN
    }
}
