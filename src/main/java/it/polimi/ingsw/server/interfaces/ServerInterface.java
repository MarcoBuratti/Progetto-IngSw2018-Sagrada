package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.server.model.Player;

import java.util.Observable;
import java.util.Observer;

public interface ServerInterface {
    void setPlayer(Player player);
    Player getPlayer();
    void send(String message);
    void close();
}
