package it.polimi.ingsw.server.interfaces;

import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.model.Player;
import java.util.Observer;

public interface ServerInterface extends Observer {

    /**
     * Allows the user to set the game parameter as the game attribute of the class.
     * @param game the Game Object the user wants to set as attribute
     */
    void setGame (Game game);

    /**
     * Allows the user to set the player parameter as the player attribute of the class.
     * @param player the Player Object representing the player
     */
    void setPlayer(Player player);

    /**
     * Returns the player attribute.
     * @return a Player Object representing the player
     */
    Player getPlayer();

    /**
     * Allows the user to send messages to the client connection.
     * @param message the message the user wants to send (String)
     */
    void send(String message);

    /**
     * Closes the connection.
     */
    void close();
}
