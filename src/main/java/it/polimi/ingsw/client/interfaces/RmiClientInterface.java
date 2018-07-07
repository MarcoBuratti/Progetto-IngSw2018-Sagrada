package it.polimi.ingsw.client.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {

    /**
     * Allows server to communicate with client.
     *
     * @param string a String containing the message that server wants to send to client
     * @throws RemoteException if the connection with the server has expired
     */
    void update(String string) throws RemoteException;

    /**
     * Allows the user to know if the connection is alive or not.
     * @return true (always)
     * @throws RemoteException if the connection isn't alive
     */
    boolean ping() throws RemoteException;
}
