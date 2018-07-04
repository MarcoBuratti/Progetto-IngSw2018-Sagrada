package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.util.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote {

    /**
     * Sets the nickname received from the client as the one of the new player and sends to the client the possible schemes for the choice.
     * @param message a String containing the names of the schemes
     * @throws RemoteException if the connection with the client has expired
     */
    void setPlayerAndAskScheme(Message message) throws RemoteException;

    /**
     * Sends to the client a message specifying whether the player has chosen the scheme in time or not.
     * Creates the new Dashboard using the scheme chosen by the user and sets it as the player's dashboard, then send it to the player.
     * @param message a String specifying whether the player has chosen the scheme in time or not and containing the scheme
     * @throws RemoteException if the connection with the client has expired
     */
    void setDashboard(Message message) throws RemoteException;

    /**
     * Allows the user to send a move to the RemoteView.
     * @param playerMove the move the player is trying to make
     * @throws RemoteException if the connection with the client has expired
     */
    void sendMove(PlayerMove playerMove) throws RemoteException;

    /**
     * Closes the connection.
     * @throws RemoteException if the connection with the client has expired
     */
    void quit() throws RemoteException;
}