package it.polimi.ingsw.server.interfaces_and_abstract_classes;

import it.polimi.ingsw.util.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote {
    void update(Message message) throws RemoteException; //IN REALTà POTRESTI USARE COME PARAMETRO IN INGRESSO LA PLAYERMOVE
    void setPlayer(Message message);
}
