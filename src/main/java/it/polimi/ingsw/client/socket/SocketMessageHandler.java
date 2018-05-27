package it.polimi.ingsw.client.socket;


import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.server.socket.SocketConnectionServer;
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
        SocketConnectionClient client = (SocketConnectionClient) clientInterface;
        client.send(chosenScheme);
    }

    private void placeDieHandler (String fromClient){
        StringTokenizer strtok = new StringTokenizer(fromClient, " ");
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
        SocketConnectionClient client = (SocketConnectionClient) clientInterface;
        client.send(json_translation);
    }

    private void goThroughHandler (){
        SocketConnectionClient client = (SocketConnectionClient) clientInterface;
        client.send("type_playerMove GoThrough");
    }

    private void quitHandler () {
        SocketConnectionClient client = (SocketConnectionClient) clientInterface;
        client.send("/quit");
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
            case 1:
                placeDieHandler(fromClient);
                break;
            case 2:
                useToolHandler(fromClient);
                break;
            case 3:
                goThroughHandler();
                break;
            case 4:
                quitHandler();
                break;
            default:
                goThroughHandler();
        }
    }

    @Override
    public void handleName(String name) {
        SocketConnectionClient client = (SocketConnectionClient) clientInterface;
        client.send(name);
    }
}