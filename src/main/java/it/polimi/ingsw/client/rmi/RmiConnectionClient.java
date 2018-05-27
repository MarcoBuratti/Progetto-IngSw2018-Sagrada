package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.RmiControllerInterface;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;
import it.polimi.ingsw.util.Message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;

public class RmiConnectionClient extends Observable implements ClientInterface, RmiClientInterface {

    RmiControllerInterface server;
    RmiServerInterface channel;
    private boolean isOn = true;

    public RmiConnectionClient(View view) {
        addObserver(view);
        try {
            server = (RmiControllerInterface) Naming.lookup("//localhost/Server");
            channel = server.addClient((RmiClientInterface) UnicastRemoteObject.exportObject(this, 0));
        } catch (MalformedURLException e) {
            System.err.println("URL non trovato!");
        } catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("Il riferimento passato non è associato a nulla!");
        }

        //DEVI CREARE IL PLAYER SULLA SERVER CONNECTION, RICORDANDOTI DI ASSOCIARE LE CONNESSIONI SUL SERVER
    }


    private synchronized void setOff() {
        isOn = false;
    }


    @Override
    public void send(String message) {
        if (getIsOn()) {
            try {
                channel.update(new Message(message));
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }

        }
    }


    private void close() {
        setOff();

    }


    public void update(String str) throws RemoteException{ //NOTIFICA LA VIEW
        if(getIsOn()) {
            setChanged();
            notifyObservers(str);
            if (str.equals("Terminate."))
                close();
        }
    }


    public void sendName(Message message) {
        if(getIsOn()) {
            try {
                this.channel.setPlayerAndAskScheme(message);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    public void sendScheme(Message message) {
        if(getIsOn()) {
            try {
                this.channel.setDashboard(message);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }


    public void sendMove(PlayerMove playerMove) {
        if(getIsOn()) {
            try {
                this.channel.sendMove(playerMove);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }


    @Override
    public synchronized boolean getIsOn() {
        return isOn;
    }

}
