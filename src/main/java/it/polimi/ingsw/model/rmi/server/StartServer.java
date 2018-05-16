package it.polimi.ingsw.model.rmi.server;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class StartServer {
    public static void main(String [] args){
        try {
            LocateRegistry.createRegistry(1099).rebind("127.0.0.1", new ChatServer() );
            ChatServer obj = new ChatServer();
            Naming.rebind("1099", obj);
            System.out.println("[System] Chat Server is ready.\n");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("Chat Server failed: " + e);
            e.printStackTrace();
        }
    }
}
