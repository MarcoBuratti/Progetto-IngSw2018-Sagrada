package it.polimi.ingsw.server.interfaces_and_abstract_classes;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiControllerInterface extends Remote {
    RmiServerInterface addClient(RmiClientInterface client) throws RemoteException;
    /* void send(RmiConnectionClient client, PlayerMove playerMove) throws RemoteException;*/
}
