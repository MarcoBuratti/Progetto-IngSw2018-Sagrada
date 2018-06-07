package it.polimi.ingsw.client.interfaces;


import java.io.IOException;
import java.util.Observable;

public interface GraphicsInterface {

    void start();
    String getNickname();
    void setHasChosenScheme(boolean bool);
    void update(Observable o, Object arg);
    String setNickname() throws IOException;
    String setIP() throws IOException;
    String setPort() throws IOException;
    String setChoice() throws IOException;
    String setScheme() throws IOException;
    String setFirstInput() throws IOException;
    String setIndexDash() throws IOException;
    String setRowColumn() throws IOException;
}
