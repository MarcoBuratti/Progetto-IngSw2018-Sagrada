package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.RmiClientInterface;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.RmiServerInterface;
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
        RmiClientInterface client = (RmiClientInterface) clientInterface;
        int choice = Integer.parseInt(fromClient);
        StringTokenizer strtok = new StringTokenizer(fromServer);
        String[] schemes = new String[4];
        for (int i=0; i<4; i++){
            String scheme = strtok.nextToken("_");
            schemes[i] = scheme;
        }
        String chosenScheme;
        switch (choice){
            case 1:
                chosenScheme = schemes[0];
            case 2:
                chosenScheme = schemes[1];
            case 3:
                chosenScheme = schemes[2];
            case 4:
                chosenScheme = schemes[3];
            default:
                chosenScheme = schemes[0];
        }
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
        RmiClientInterface client = (RmiClientInterface) clientInterface;
        client.sendMove(PlayerMove.PlayerMoveReader(jsonObject));

    }

    @Override
    public void handleName(String name){
        RmiClientInterface client = (RmiClientInterface) clientInterface;
        client.sendName(new Message(name));
        //E' bruttino, pensa ad un'altra soluzione sfruttando la ClientInterface
    }
}
