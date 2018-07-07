package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class Player {
    private String nickname;
    private int currentFavourToken;
    private Dashboard dashboard;
    private PrivateAchievement privateAchievement;
    private boolean skipSecondTurn;

    /**
     * Creates a Player object, which represent a player, having his own nickname and having his dashboard
     * and private achievement randomly assigned by the Game board.
     * The nickname argument specifies the nickname the player used to log in.
     * The dashboard and privateAchievement argument are randomly assigned by the game board.
     * The currentFavourToken attribute specifies the number of token left and is initialized as
     * the number of favour token associated with the scheme chosen from the player at the
     * start of the game.
     *
     * @param nickname the player's nickname, chosen during the log in session
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.skipSecondTurn = false;
    }

    /**
     * Returns the player's nickname attribute as a String object.
     *
     * @return a String object representing the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns a Dashboard object representing the player's dashboard attribute and containing the chosen scheme
     * and the initial number of token.
     *
     * @return a Dashboard object representing the player's dashboard
     */
    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     * Allows the user to create a new dashboard from the selected scheme and associates it to the player.
     *
     * @param scheme the name of the scheme chosen by the player
     * @throws NotValidValueException if one of the restrictions, contained in the json file associated to the scheme,
     *                                specifies a not allowed value
     */
    public void setDashboard(String scheme) throws NotValidValueException {
        this.dashboard = new Dashboard(scheme);
        this.currentFavourToken = this.dashboard.getFavourToken();
    }

    /**
     * Returns an int representing the number of favour tokens that the player currently owns.
     *
     * @return an int which represents the number of favour tokens left.
     */
    public int getCurrentFavourToken() {
        return currentFavourToken;
    }

    /**
     * Returns a PrivateAchievement object representing the player's private achievement and associated
     * with one of the available colours.
     *
     * @return the player's private achievement as a Private Achievement object.
     */
    public PrivateAchievement getPrivateAchievement() {
        return privateAchievement;
    }

    /**
     * Allows the user to set the player's private achievement
     *
     * @param privateAchievement the private achievement the user wants to set as privateAchievement attribute of the Player Object
     */
    public void setPrivateAchievement(PrivateAchievement privateAchievement) {
        this.privateAchievement = privateAchievement;
    }

    /**
     * Returns a boolean which specifies whether the player has enough favour tokens to use
     * the selected tool or not.
     *
     * @param usedTool specifies whether the tool has been used at least once
     */
    public boolean hasEnoughToken(boolean usedTool) {
        if (this.currentFavourToken > 0) {
            if (!usedTool)
                return true;
            else {
                return this.currentFavourToken > 1;
            }
        } else return false;
    }

    /**
     * Allows the player to use his favour tokens in order to use a tool.
     * The cost of each tool is 1 favour token if it has never been used by any player
     * or 2 favour tokens if it has already been used.
     * The argument usedTool must specify whether the tool has already been used by
     * any of the players or not.
     *
     * @param usedTool a boolean specifying whether the tool the user wants to use has been already used or not
     */
    public void useToken(boolean usedTool) {
        if (usedTool)
            this.currentFavourToken = this.currentFavourToken - 2;
        else
            this.currentFavourToken--;
    }

    /**
     * Returns a boolean which specifies whether the player has to skip his second turn or not.
     *
     * @return the skipSecondTurn attribute
     */
    public boolean skipSecondTurn() {
        return skipSecondTurn;
    }

    /**
     * Allows the user to set the skipSecondTurn attribute.
     *
     * @param skipSecondTurn the boolean the user wants to set as skipSecondTurn attribute
     */
    public void setSkipSecondTurn(boolean skipSecondTurn) {
        this.skipSecondTurn = skipSecondTurn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Player: " + this.getNickname() + "!" +
                "Current Token: " + this.getCurrentFavourToken() + "!" +
                dashboard.toString();
    }
}
