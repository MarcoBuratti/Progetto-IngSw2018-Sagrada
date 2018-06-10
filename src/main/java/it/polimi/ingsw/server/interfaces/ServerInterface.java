package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.model.Player;
import java.util.Observer;

public interface ServerInterface extends Observer {

    void setGame (Game game);

    void setPlayer(Player player);

    Player getPlayer();

    void send(String message);

    void close();
}
