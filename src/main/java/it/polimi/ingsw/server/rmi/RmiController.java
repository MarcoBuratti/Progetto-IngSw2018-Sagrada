package it.polimi.ingsw.server.rmi;

import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.RmiControllerInterface;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.RmiServerInterface;
import it.polimi.ingsw.server.model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiController extends UnicastRemoteObject implements RmiControllerInterface {

    Server server;


    public RmiController(Server server) throws RemoteException {
        super(0);
        this.server = server;
    }

    public synchronized RmiServerInterface addClient(RmiClientInterface client) throws RemoteException {
        RmiServerInterface connectionRMI = new RmiConnectionServer(client, server);
        return ((RmiServerInterface) UnicastRemoteObject.exportObject(connectionRMI, 0));
    }

}