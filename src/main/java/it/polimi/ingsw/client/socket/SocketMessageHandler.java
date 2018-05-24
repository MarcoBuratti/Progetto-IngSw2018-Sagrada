package it.polimi.ingsw.client.socket;


import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.util.MessageHandler;

import java.util.StringTokenizer;

public class SocketMessageHandler implements MessageHandler {

    ClientInterface clientInterface;

    public SocketMessageHandler(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }

    @Override
    public void handleScheme (String fromServer, String fromClient) {
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
        clientInterface.send(chosenScheme);
    }

    private void placeDieHandler (String fromClient){
        StringTokenizer strtok = new StringTokenizer(fromClient);
        String move = strtok.nextToken();

        StringBuilder bld = new StringBuilder();
        String json_translation;
        bld.append("type_playerMove PlaceDie");
        int i = 1;
        while(strtok.hasMoreTokens()){
            String key = "Key" + i;
            String value = strtok.nextToken();
            bld.append(" " + key + " " + value);
            i++;
        }
        json_translation = bld.toString();
        clientInterface.send(json_translation);
    }

    private void goThroughHandler (){
        clientInterface.send("type_playerMove GoThrough");
    }

    private void quitHandler () {
        clientInterface.send("/quit");
    }

    private void useToolHandler (String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        String move = strtok.nextToken();

        StringBuilder bld = new StringBuilder();
        String json_translation;
        bld.append("type_playerMove UseTool");
        int i = 1;
        while(strtok.hasMoreTokens()){
            //DA IMPLEMENTARE
        }
    }

    @Override
    public void handleMove(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        int moveChoice = Integer.parseInt(strtok.nextToken());
        switch (moveChoice){
            case 1: placeDieHandler(fromClient);

            case 2: useToolHandler(fromClient);
            case 3: goThroughHandler();
            case 4: quitHandler();
            default: goThroughHandler();
        }
    }

    @Override
    public void handleName(String name) {

    }
}