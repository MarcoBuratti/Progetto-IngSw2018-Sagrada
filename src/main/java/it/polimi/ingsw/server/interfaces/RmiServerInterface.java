package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.util.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote {
    void setPlayerAndAskScheme(Message message) throws RemoteException;
    void setDashboard (Message message) throws RemoteException;
    void sendMove (PlayerMove playerMove) throws RemoteException;
}