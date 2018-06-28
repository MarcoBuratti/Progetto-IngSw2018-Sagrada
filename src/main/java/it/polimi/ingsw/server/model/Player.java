package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotEnoughFavourTokensLeft;
import it.polimi.ingsw.server.model.exception.NotValidValueException;

public class Player {
    private String nickname;
    private int currentFavourToken;
    private Dashboard dashboard;
    private PrivateAchievement privateAchievement;
    private boolean skipSecondTurn;
    private ServerInterface serverInterface;


    public Player(String nickname) {
        this.nickname = nickname;
        this.skipSecondTurn = false;
    }

    public void setDashboard(String scheme) throws NotValidValueException {
        this.dashboard = new Dashboard(scheme);
        this.dashboard.setOwner(this);
        this.currentFavourToken = this.dashboard.getFavourToken();
    }

    public void setPrivateAchievement(PrivateAchievement privateAchievement) {
        this.privateAchievement = privateAchievement;
    }

    /**
     * Creates a Player object, which represent a player, having his own nickname and having his dashboard
     * and private achievement randomly assigned by the Game board.
     * The nickname argument specifies the nickname the player used to log in.
     * The dashboard and privateAchievement argument are randomly assigned by the game board.
     * The currentFavourToken attribute specifies the number of token left and is initialized as
     * the number of favour token associated with the scheme chosen from the player at the
     * start of the game.
     *
     * @param nickname           the player's nickname, chosen during the log in session
     * @param dashboard          the player's dashboard, containing the chosen scheme and the initial number of favour tokens
     * @param privateAchievement the player's private achievement, associated with one of the available colours
     */


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
     * Allows the player to use his favour tokens in order to use a tool.
     * The cost of each tool is 1 favour token if it has never been used by any player
     * or 2 favour tokens if it has already been used.
     * The argument usedTool must specify if the tool has already been used by
     * any of the players.
     *
     * @param usedTool specifies whether the tool has been used at least once
     * @throws NotEnoughFavourTokensLeft if the player does not have enough tokens left
     */
    public void useToken(Boolean usedTool) throws NotEnoughFavourTokensLeft {
        if (this.currentFavourToken > 0) {
            if (!usedTool)
                this.currentFavourToken--;
            else {
                if (this.currentFavourToken > 1)
                    this.currentFavourToken -= 2;
                else throw new NotEnoughFavourTokensLeft();
            }
        } else throw new NotEnoughFavourTokensLeft();
    }

    public boolean skipSecondTurn() {
        return skipSecondTurn;
    }

    public void setSkipSecondTurn(boolean skipSecondTurn) {
        this.skipSecondTurn = skipSecondTurn;
    }

    @Override
    /**
     * Returns a string which represent the Player object, specifying the player's user name, his dashboard,
     * how many favour tokens he/she currently has and his/her private achievement.
     */
    public String toString() {
        StringBuilder bld = new StringBuilder();
        bld.append("Player: " + this.getNickname() + "!");
        bld.append("Current Token: " + this.getCurrentFavourToken() + "!");
        bld.append(dashboard.toString());
        return bld.toString();
    }
}
