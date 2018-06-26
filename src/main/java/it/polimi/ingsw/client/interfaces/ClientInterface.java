package it.polimi.ingsw.client.interfaces;

public interface ClientInterface {

    /**
     * Returns a boolean specifying if the connection is on or not.
     * @return a boolean which specifies the connection state
     */
    boolean getIsOn();
    /**
     * Sets the playerNickname attribute.
     * @param nickname the String the user wants to set as playerNickname attribute.
     */
    void setPlayerNickname(String nickname);
    /**
     * Sends the chosen nickname to the server.
     * @param name a String specifying the chosen nickname
     */
    void handleName(String name);
    /**
     * Deals with the client input in order to select the chosen scheme correctly.
     * @param fromServer a String containing the names of the available schemes
     * @param fromClient a String containing the client input
     */
    void handleScheme(String fromServer, String fromClient);
    /**
     * Deals with the client input in order to build the correct move.
     * @param fromClient a String containing the client input
     */
    void handleMove(String fromClient);

    void setTool(String s);

    void game();

    void setIsOn(boolean bool);

    void setContinueToPlay(boolean bool);
}
