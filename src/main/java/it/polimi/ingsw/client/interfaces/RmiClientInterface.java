package it.polimi.ingsw.client.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {
    void update(String str) throws RemoteException;

}
