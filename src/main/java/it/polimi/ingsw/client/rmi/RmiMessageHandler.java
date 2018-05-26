package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.util.Message;
import it.polimi.ingsw.util.MessageHandler;
import org.json.simple.JSONObject;

import java.util.StringTokenizer;

public class RmiMessageHandler implements MessageHandler {
    private ClientInterface clientInterface;

    public RmiMessageHandler (ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }

    @Override
    public synchronized void handleScheme(String fromServer, String fromClient) {
        RmiConnectionClient client = (RmiConnectionClient) clientInterface;
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
        switch (choice){
            case 1:
                chosenScheme = schemes[0];
                break;
            case 2:
                chosenScheme = schemes[1];
                break;
            case 3:
                chosenScheme = schemes[2];
                break;
            case 4:
                chosenScheme = schemes[3];
                break;
            default:
                chosenScheme = schemes[0];
        }
        System.out.println("You have chosen the following scheme: " + chosenScheme);
        client.sendScheme(new Message(chosenScheme));
    }

    @Override
    public void handleMove(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        String key, value;
        JSONObject jsonObject = new JSONObject();
        while(strtok.hasMoreTokens()){
            key = strtok.nextToken();
            value = strtok.nextToken();
            jsonObject.put(key, value);
        }
        RmiConnectionClient client = (RmiConnectionClient) clientInterface;
        client.sendMove(PlayerMove.PlayerMoveReader(jsonObject));

    }

    @Override
    public void handleName(String name){
        RmiConnectionClient client = (RmiConnectionClient) clientInterface;
        client.sendName(new Message(name));
    }
}
