package it.polimi.ingsw.client.rmi;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.server.interfaces_and_abstract_classes.RmiServerInterface;
import it.polimi.ingsw.util.Message;
import it.polimi.ingsw.util.MessageHandler;

public class RmiMessageHandler implements MessageHandler {
    private ClientInterface clientInterface;

    public RmiMessageHandler (ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }

    @Override
    public void handleScheme(String fromServer, String fromClient) {
    }

    @Override
    public void handleMove(String fromClient) {
    }

    @Override
    public void handleName(String name){
        RmiServerInterface client = (RmiServerInterface) clientInterface;
        client.setPlayer(new Message(name));
        //DEVO TROVARE UN MODO PER FAR CAPIRE AL SERVER CHE STO INVIANDO IL NOME
    }
}
