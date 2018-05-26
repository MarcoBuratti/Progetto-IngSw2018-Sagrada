package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.server.model.Player;

import java.util.Observer;

public interface ServerInterface extends Observer {
    void setPlayer(Player player);
     Player getPlayer();
    boolean getYourTurn();
    void setYourTurn(boolean bool);
    void send(String message);
   void close();
}
