package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.RmiControllerInterface;
import it.polimi.ingsw.server.interfaces.RmiServerInterface;
import it.polimi.ingsw.util.Message;
import org.json.simple.JSONObject;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.StringTokenizer;

public class RmiConnectionClient extends Observable implements ClientInterface, RmiClientInterface {

    RmiControllerInterface server;
    RmiServerInterface channel;
    private boolean isOn = true;
    private String playerNickname;

    /**
     * Creates a RmiConnectionClient object, adding the corresponding view to its observers and establishing a connection between it and the server.
     * @param view the View object which has to be added to the observers.
     * @param address the server address
     * @param port the port that has to be used by the remote object to receive incoming calls
     */
    public RmiConnectionClient(View view, String address, int port) {
        addObserver(view);
        try {
            server = (RmiControllerInterface) Naming.lookup("//" + address + "/Server");
            channel = server.addClient((RmiClientInterface) UnicastRemoteObject.exportObject(this, port));
        } catch (Exception e) {
            System.out.println("Connection error: " + e.toString());
        }

    }

    /**
     * Tries to call the server method which saves the nickname and sends back the possible schemes.
     * @param message a Message object containing a String which specifies the chosen nickname
     */
    private void sendName(Message message) {
        if (getIsOn()) {
            try {
                this.channel.setPlayerAndAskScheme(message);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    /**
     * Tries to call the server method which initializes the player's dashboard using the chosen scheme.
     * @param message a Message object containing a String which specifies the chosen scheme
     */
    private void sendScheme(Message message) {
        if (getIsOn()) {
            try {
                this.channel.setDashboard(message);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    /**
     * Tries to call the server method which forwards the PlayerMove object to the RemoteView.
     * @param playerMove a PlayerMove object specifying the move the player is trying to make
     */
    private void sendMove(PlayerMove playerMove) {
        if (getIsOn()) {
            try {
                this.channel.sendMove(playerMove);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    /**
     * Sets the boolean attribute isOn as false whenever the connection needs to be closed.
     */
    private synchronized void close() {
        this.isOn = false;
    }


    /**
     * Allows the user to close the connection between this client and the server.
     */
    public void quit() {
        try {
            this.channel.quit();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.close();
    }

    /**
     * Returns the current state of the boolean attribute isOn.
     * @return a boolean which specifies the connection state
     */
    @Override
    public synchronized boolean getIsOn() {
        return isOn;
    }

    /**
     * Allows the user to set the playerNickname attribute as the player's nickname.
     * @param nickname the String the user wants to set as playerNickname attribute.
     */
    @Override
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    /**
     * Sends the chosen nickname to the server as a new Message object containing the corresponding String.
     * @param name a String specifying the chosen nickname
     */
    @Override
    public void handleName(String name) {
        sendName(new Message(name));
    }

    /**
     * Deals with the client input in order to select the chosen scheme correctly and sends it to the server
     * through the sendScheme method.
     * @param fromServer a String containing the names of the available schemes
     * @param fromClient a String containing the client input
     */
    @Override
    public void handleScheme(String fromServer, String fromClient) {
        int choice = Integer.parseInt(fromClient);
        String substringSchemes = fromServer.substring(fromServer.indexOf('.') + 2);
        StringTokenizer strtok = new StringTokenizer(substringSchemes, ",");
        String[] schemes = new String[4];
        int i = 0;
        while (strtok.hasMoreTokens()) {
            schemes[i] = strtok.nextToken();
            i++;
        }
        String chosenScheme;
        if (choice > 0 && choice <= 4)
            chosenScheme = schemes[choice - 1];
        else
            chosenScheme = schemes[0];
        sendScheme(new Message(chosenScheme));
    }

    /**
     * Deals with the client input in order to build the correct move as a PlayerMove object and sends it
     * to the server through the sendMove method.
     * @param fromClient a String containing the client input
     */
    @Override
    public void handleMove(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        String key;
        String value;
        final String TYPE_PLAYERMOVE = "type_playerMove";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerID", this.playerNickname);
        String moveType = strtok.nextToken();
        int choice = Integer.parseInt(moveType);
        switch (choice) {
            case 1:
                jsonObject.put(TYPE_PLAYERMOVE, "PlaceDie");
                break;
            case 2:
                jsonObject.put(TYPE_PLAYERMOVE, "UseTool");
                break;
            case 3:
                jsonObject.put(TYPE_PLAYERMOVE, "GoThrough");
                break;
        }
        if (choice > 0 && choice <= 3) {
            int i = 1;
            while (strtok.hasMoreTokens()) {
                key = "Key" + i;
                value = strtok.nextToken();
                jsonObject.put(key, value);
                i++;
            }
            sendMove(PlayerMove.playerMoveReader(jsonObject));
        } else if (choice == 4) {
            quit();
        }
    }


    /**
     * Checks if the connection is on and notifies the observers (View) that a new message has arrived from the server.
     * Closes the connection if the message is "Terminate.".
     * @param str a String containing the message sent by server
     * @throws RemoteException
     */
    @Override
    public void update(String str) throws RemoteException {
        if (getIsOn()) {
            setChanged();
            notifyObservers(str);
            if (str.equals("Terminate."))
                close();
        }
    }
}
