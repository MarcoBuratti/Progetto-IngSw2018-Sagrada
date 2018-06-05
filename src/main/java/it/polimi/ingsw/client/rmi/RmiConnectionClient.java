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

    public RmiConnectionClient(View view, String address, int port) {
        addObserver(view);
        try {
            server = (RmiControllerInterface) Naming.lookup("//"+ address +"/Server");
            channel = server.addClient((RmiClientInterface) UnicastRemoteObject.exportObject(this, port));
        } catch (Exception e) {
            System.out.println("Connection error: " + e.toString());
        }

    }


    private synchronized void close() {
        this.isOn = false;
    }

    public void update(String str) throws RemoteException{ //NOTIFICA LA VIEW
        if(getIsOn()) {
            setChanged();
            notifyObservers(str);
            if (str.equals("Terminate."))
                close();
        }
    }


    private void sendName(Message message) {
        if(getIsOn()) {
            try {
                this.channel.setPlayerAndAskScheme(message);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    private void sendScheme(Message message) {
        if(getIsOn()) {
            try {
                this.channel.setDashboard(message);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    private void sendMove(PlayerMove playerMove) {
        if(getIsOn()) {
            try {
                this.channel.sendMove(playerMove);
            } catch (RemoteException e) {
                System.err.println(e.toString());
                close();
            }
        }
    }

    public void quit(){
        try {
            this.channel.quit();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.close();
    }

    @Override
    public synchronized boolean getIsOn() {
        return isOn;
    }

    @Override
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    @Override
    public void handleScheme(String fromServer, String fromClient) {
        int choice = Integer.parseInt(fromClient);
        String substringSchemes = fromServer.substring(fromServer.indexOf(".") + 2);
        StringTokenizer strtok = new StringTokenizer(substringSchemes, ",");
        String[] schemes = new String[4];
        int i = 0;
        while(strtok.hasMoreTokens()){
            schemes[i] = strtok.nextToken();
            i++;
        }
        String chosenScheme;
        if(choice > 0 && choice <= 4)
            chosenScheme = schemes[choice-1];
        else
            chosenScheme = schemes[0];
        sendScheme(new Message(chosenScheme));
    }

    @Override
    public void handleMove(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        String key, value;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerID", this.playerNickname);
        String moveType = strtok.nextToken();
        int choice = Integer.parseInt(moveType);
        switch (choice){
            case 1:
                jsonObject.put("type_playerMove", "PlaceDie");
                break;
            case 2:
                jsonObject.put("type_playerMove", "UseTool");
                break;
            case 3:
                jsonObject.put("type_playerMove", "GoThrough");
                break;
        }
        if ( choice > 0 && choice <= 3 ) {
            int i = 1;
            while (strtok.hasMoreTokens()) {
                key = "Key" + i;
                value = strtok.nextToken();
                jsonObject.put(key, value);
                i++;
            }
        sendMove(PlayerMove.PlayerMoveReader(jsonObject));
        }
        else if ( choice == 4 ){
            quit();
        }
    }

    @Override
    public void handleName(String name) {
        sendName(new Message(name));
    }
}
