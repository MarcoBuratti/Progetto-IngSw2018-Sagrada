package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.interfaces.RmiControllerInterface;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiController extends UnicastRemoteObject implements RmiControllerInterface {

    private Server server;

    /**
     * Creates a RmiController Object, setting its server attribute as the server who called the constructor.
     *
     * @param server the server
     * @throws RemoteException if there is a communication-related problem
     */
    public RmiController(Server server) throws RemoteException {
        super(0);
        this.server = server;
    }

    /**
     * Creates a RmiConnectionServer Object (connecting it to the client connection and to the server) and returns it as a RmiServerInterface,
     * exporting it through UnicastRemoteObject as a RemoteStub Object (using default port).
     *
     * @param client the RmiClientInterface Object the new RmiServerInterface needs to be connected to
     * @return an RmiServerInterface (always returns a RmiConnectionServer
     * @throws RemoteException if there is a communication-related problem
     */
    public synchronized RmiServerInterface addClient(RmiClientInterface client) throws RemoteException {
        RmiServerInterface connectionRMI = new RmiConnectionServer(client, server);
        return ((RmiServerInterface) UnicastRemoteObject.exportObject(connectionRMI, 0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (object != null && object.getClass() == this.getClass()) {
            RmiController rmiController = (RmiController) object;
            return this.server.equals(rmiController.server);
        } else return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //Please note this method is not used, but it's always recommended to override hashCode if equals is overridden
    public int hashCode() {
        return super.hashCode();
    }


}