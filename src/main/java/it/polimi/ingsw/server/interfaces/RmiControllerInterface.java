package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiControllerInterface extends Remote {

    /**
     * Allows the user to connect the client to the server, allowing the RmiConnectionClient class
     * to call the related RmiConnectionServer its remote methods.
     *
     * @param client a RmiClientInterface exported as a RemoteStub Object
     * @return a RmiServerInterface representing the server thread associated with the connection
     * @throws RemoteException if there is a communication-related problem
     */
    RmiServerInterface addClient(RmiClientInterface client) throws RemoteException;
}
