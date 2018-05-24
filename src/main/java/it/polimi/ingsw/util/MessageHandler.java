package it.polimi.ingsw.util;

public interface MessageHandler {
    void handleScheme (String fromServer, String fromClient);
    void handleMove(String fromClient);
    void handleName(String name);
}
