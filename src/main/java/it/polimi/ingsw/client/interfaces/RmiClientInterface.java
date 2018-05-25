package it.polimi.ingsw.client.interfaces;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.util.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {
    void update(String str) throws RemoteException;
    void sendName (Message message);
    void sendScheme (Message message);
    void sendMove (PlayerMove playerMove);
}
