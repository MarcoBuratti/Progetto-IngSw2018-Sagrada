package it.polimi.ingsw.model.rmi.interfaces;

import java.rmi.*;

public interface MessengerInterface  extends Remote{

    String getUsername() throws RemoteException;
    void tell(String s) throws RemoteException;
}