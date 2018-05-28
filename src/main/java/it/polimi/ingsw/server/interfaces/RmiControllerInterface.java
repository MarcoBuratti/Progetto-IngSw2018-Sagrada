package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiControllerInterface extends Remote {
    RmiServerInterface addClient(RmiClientInterface client) throws RemoteException;
}
