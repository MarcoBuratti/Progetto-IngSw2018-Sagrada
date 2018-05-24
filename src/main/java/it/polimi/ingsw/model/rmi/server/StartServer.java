package it.polimi.ingsw.model.rmi.server;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class StartServer {
    public static void main(String [] args){
        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind("server",new ChatServer());
            System.out.println("[System] Chat Server is ready.\n");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("Chat Server failed: " + e);
            e.printStackTrace();
        }
    }
}
