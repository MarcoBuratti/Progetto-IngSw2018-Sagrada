package it.polimi.ingsw.client.interfaces;

public interface ClientInterface {
    boolean getIsOn();

    void setPlayerNickname(String nickname);

    void handleScheme(String fromServer, String fromClient);

    void handleMove(String fromClient);

    void handleName(String name);
}
