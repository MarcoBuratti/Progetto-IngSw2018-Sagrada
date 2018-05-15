package it.polimi.ingsw.controller.rmi.server;


import it.polimi.ingsw.controller.rmi.interfaces.ChatInterface;
import it.polimi.ingsw.controller.rmi.interfaces.MessengerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServer extends UnicastRemoteObject implements ChatInterface {

    private Hashtable l = new Hashtable();
    public ChatServer() throws RemoteException{}

    @Override
    public boolean login(MessengerInterface m) throws RemoteException {
       l.put(m.getUsername(), m);
       m.tell("[Server] Welcome Client " + m.getUsername());
       return true;
    }

    @Override
    public void sendToAll(String s, MessengerInterface from) throws RemoteException {
        System.out.println("\n[" + from.getUsername() + "] " + s);
        Enumeration usernames = l.keys();
        while (usernames.hasMoreElements()){
            String user = (String) usernames.nextElement();
            MessengerInterface m = (MessengerInterface)l.get(user);
            if(user.equals(from.getUsername())) {continue;}
            try {
                m.tell("\n[" + from.getUsername() + "] " +s);
            }catch (Exception e){ e.printStackTrace(); }
        }
    }

    @Override
    public MessengerInterface getMessenger(String username) throws RemoteException {
        MessengerInterface m = (MessengerInterface)l.get(username);
        return m;
    }
}
