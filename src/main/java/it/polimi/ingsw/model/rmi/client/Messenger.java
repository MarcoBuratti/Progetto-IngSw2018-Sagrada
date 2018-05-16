package it.polimi.ingsw.model.rmi.client;;

import it.polimi.ingsw.model.rmi.interfaces.ChatInterface;
import it.polimi.ingsw.model.rmi.interfaces.MessengerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Messenger extends UnicastRemoteObject implements MessengerInterface {

    private String username;
    private ChatInterface server;
    public Messenger(String u, ChatInterface s) throws RemoteException{
        username = u;
        server = s;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public void tell(String s) throws RemoteException {
        System.out.println(s);
    }
}
