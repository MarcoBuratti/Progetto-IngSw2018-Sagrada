package it.polimi.ingsw.server.interfaces_and_abstract_classes;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.util.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote {
    void update(Message message) throws RemoteException; //IN REALTà POTRESTI USARE COME PARAMETRO IN INGRESSO LA PLAYERMOVE
    void setPlayerAndAskScheme(Message message) throws RemoteException;
    void setDashboard (Message message) throws RemoteException;
    void sendMove (PlayerMove playerMove) throws RemoteException;
}