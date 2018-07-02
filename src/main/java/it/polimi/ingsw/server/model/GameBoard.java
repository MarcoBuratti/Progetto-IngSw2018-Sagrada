package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.tool.Tool;
import it.polimi.ingsw.server.controller.tool.ToolFactory;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.achievement.*;

import java.util.*;

public class GameBoard extends Observable {
    private static final int NUMBER_OF_PUBLIC_ACHIEVEMENTS = 3;
    private static final int NUMBER_OF_TOOLS = 3;
    private RoundTrack roundTrack;
    private ArrayList<Player> players;
    private DiceBag diceBag;
    private ArrayList<CardAchievement> publicAchievements;
    private ArrayList<Tool> tools;
    private ArrayList<Die> draftPool;
    private Player currentPlayer;

    /**
     * Creates a GameBoard object which represents the game board, containing all the objects used during the game.
     * When the GameBoard object is created, the following objects are created too:
     * - The dice bag containing all the dice needed for a new game (DiceBag Object);
     * - The round track containing no dice, as the first round has not been played yet (RoundTrack Object);
     * - The players list containing the references to the Player Objects;
     * - The public achievements list containing the randomly extracted CardAchievement Objects;
     * - The tool cards list containing the randomly extracted Tool Objects.
     * The schemes argument must specify the players' user names and chosen window schemes.
     */

    public GameBoard( List<Player> players) {

        this.players = new ArrayList<>(players);
        publicAchievements = new ArrayList<>();
        tools = new ArrayList<>();

        this.roundTrack = new RoundTrack();
        this.diceBag = new DiceBag();

        List<PublicAchievementNames> publicAchievementList = Arrays.asList(PublicAchievementNames.values());
        Collections.shuffle(publicAchievementList);
        AbstractCardFactory abstractFactory = new CardFactory();
        for (int i = 0; i < NUMBER_OF_PUBLIC_ACHIEVEMENTS; i++) {
            CardAchievement publicAchievementsFactory = abstractFactory.extractCardAchievement(publicAchievementList.get(i));
            this.publicAchievements.add(publicAchievementsFactory);
        }

        List<ToolNames> toolList = Arrays.asList(ToolNames.values());
        Collections.shuffle(toolList);
        ToolFactory abstractToolFactory = new ToolFactory();
        for (int i = 0; i < NUMBER_OF_TOOLS; i++) {
            Tool toolFactory = abstractToolFactory.getTool(toolList.get(i));
            this.tools.add(toolFactory);
        }

        List<Color> privateAchievementsList = Arrays.asList(Color.values());
        Collections.shuffle(privateAchievementsList);
        int i = 0;

        try {
            for (Player p : players) {
                p.setPrivateAchievement(new PrivateAchievement(privateAchievementsList.get(i)));
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        setChanged();
        notifyObservers(this);
    }

    /**
     * Returns a RoundTrack object representing the current game's round track.
     *
     * @return the round track used in the current game
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Returns a DiceBag object representing the current game's dice bag.
     *
     * @return the dice bag used in the current game
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * Returns an ArrayList containing Player objects which represent the players.
     *
     * @return the players list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns an ArrayList containing CardAchievements objects which represent the public achievement extracted
     * for the current game.
     *
     * @return the public achievements list
     */
    public List<CardAchievement> getPublicAchievements() {
        return publicAchievements;
    }

    /**
     * Returns an ArrayList containing Die objects. The ArrayList is a copy of the draft pool used
     * for the current round (updated at the start of every round during the game).
     *
     * @return a copy of the draft pool used for the current round
     */
    public List<Die> getDraftPool() {
        return new ArrayList<>(this.draftPool);
    }

    /**
     * Allows the user to change the reference for the draftPool attribute, in order to
     * initialize a new draft pool at the start of each round.
     *
     * @param draftPool the new draft pool created at the start of the round
     */
    public void setDraftPool(List<Die> draftPool) {
        this.draftPool = new ArrayList<>(draftPool);
        setChanged();
        notifyObservers(this);
    }

    /**
     * Allows the user to remove all the Die objects from the draftPool in order to empty it.
     */
    public void emptyDraftPool() {
        if (this.draftPool != null)
            this.draftPool.clear();
    }

    /**
     * Allows the user to remove a Die object from the draftPool.
     * The die argument must specify which Die object the user wants to remove from the draftPool.
     *
     * @param die the die the user wants to remove from the draft pool
     */
    public void removeDieFromDraftPool(Die die) {
        try {
            this.draftPool.remove(draftPool.indexOf(die));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        setChanged();
        notifyObservers(this);
    }

    /**
     * Allows the user to change a selected die with one contained in the draftPool.
     *
     * @param die the die the user wants to put in the draftPool
     * @param dieIndex the index of the die the user wants to get from the draftPool
     * @return the die removed from the draftPool
     */
    public Die changeDie(Die die, int dieIndex) {
        Die myDie = this.draftPool.remove(dieIndex);
        this.draftPool.add(dieIndex, die);
        return myDie;
    }

    /**
     * Allows the user to set the player whose playing the current turn.
     * @param currentPlayer a Player Object referring to the current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns a string telling who is the current player.
     * @return a String Object which specifies who is the current player
     */
    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        if (currentPlayer != null)
            bld.append("\nNow it's ").append(currentPlayer.getNickname()).append("'s turn.");
        return bld.toString();
    }

    /**
     * Returns a representation of the extracted tools.
     * @return a String Object which specifies the extracted tools
     */
    public String sendTool() {
        StringBuilder bld = new StringBuilder();
        bld.append("Tools-");
        for (Tool t : tools) {
            bld.append(t.getToolName());
            bld.append(",").append(t.isAlreadyUsed());
            bld.append(",");
        }
        bld.append("-");
        return bld.toString();
    }

    /**
     * Returns a representation of the extracted public achievements.
     * @return a String Object which specifies the extracted public achievements
     */
    public String sendAchievement() {
        StringBuilder bld = new StringBuilder();
        bld.append("Public Achievements-");
        for (CardAchievement p : publicAchievements) {
            bld.append(p.toString());
        }
        bld.append("-");
        return bld.toString();
    }

    /**
     * Returns a representation of the roundTrack.
     * @return a String Object which represents the roundTrack
     */
    public String sendRoundTrack() {
        return roundTrack.toString() +
                "-";
    }

    /**
     * Returns a representation of the draftPool.
     * @return a String Object which represents the draftPool
     */
    public String sendDraft() {
        StringBuilder bld = new StringBuilder();
        StringBuilder bl = new StringBuilder();
        for (Die die : draftPool) {
            bld.append(die.toString());
            bld.append(" ");
        }
        if(bld.length() > 0) {
            bl.append("Draft-").append(bld);
            bl.append("-");
        }
        return bl.toString();
    }

    /**
     * Returns a list of the extracted tools.
     * @return an ArrayList Object containing the extracted Tool Objects
     */
    public List<Tool> getTools() {
        return this.tools;
    }

    /**
     * This method notifies its Observers that something has been changed.
     */
    public void update () {
        setChanged();
        notifyObservers(this);
    }


}