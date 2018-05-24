package it.polimi.ingsw.server.interfaces_and_abstract_classes;
import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class ServerAbstractClass extends Observable implements Observer {
    public abstract void setPlayer(Player player);
    public abstract Player getPlayer();
    public abstract boolean getYourTurn();
    public abstract void setYourTurn(boolean bool);
    public abstract void send(String message);
    public abstract void close();
    public abstract String askForChosenScheme() throws IOException;
}
