package it.polimi.ingsw.model.rmi.interfaces;

import java.rmi.*;

public interface ChatInterface extends Remote {

    boolean login(MessengerInterface m) throws RemoteException;
    void sendToAll(String s, MessengerInterface from) throws RemoteException;
    MessengerInterface getMessenger(String username)  throws RemoteException;
}