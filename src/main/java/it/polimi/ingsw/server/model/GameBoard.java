package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.tool.Tool;
import it.polimi.ingsw.server.controller.tool.ToolFactory;
import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.achievement.*;
import java.util.*;

public class GameBoard extends Observable{
    private static final int NUMBER_OF_PUBLIC_ACHIEVEMENTS = 3;
    private static final int NUMBER_OF_TOOL = 3;
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
     * - The public achievements list containing the randomly extracted CardAchievement Objects.
     * The schemes argument must specify the players' user names and chosen window schemes.
     * @param schemes a map in which keys represent the players' user names and
     * values represent the names of the chosen window schemes
     */
    public GameBoard (Map<String, String> schemes) {

        players = new ArrayList<>();
        publicAchievements = new ArrayList<>();
        tools = new ArrayList<>();

        this.roundTrack = new RoundTrack();
        this.diceBag = new DiceBag();

        List<PublicAchievementNames> publicAchievementList = Arrays.asList(PublicAchievementNames.values());
        Collections.shuffle(publicAchievementList);
        AbstractCardFactory abstractFactory = new CardFactory();
        for ( int i = 0; i < NUMBER_OF_PUBLIC_ACHIEVEMENTS; i++ ) {
            CardAchievement publicAchievementsFactory = abstractFactory.extractCardAchievement(publicAchievementList.get(i));
            this.publicAchievements.add(publicAchievementsFactory);
        }

        List<ToolNames> toolList = Arrays.asList(ToolNames.values());
        Collections.shuffle(toolList);
        ToolFactory abstractToolFactory = new ToolFactory();
        for ( int i = 0; i < NUMBER_OF_TOOL; i++ ) {
            Tool toolFactory = abstractToolFactory.getTool(toolList.get(i));
            this.tools.add(toolFactory);
        }

        List<Color> privateAchievementsList = Arrays.asList(Color.values());
        Collections.shuffle(privateAchievementsList);
        int i = 0;

        try {
            for (Map.Entry<String, String> elem : schemes.entrySet()) {
                players.add(new Player(elem.getKey(), new Dashboard(elem.getValue()), new PrivateAchievement(privateAchievementsList.get(i))));
                players.get(i).getDashboard().setOwner(players.get(i));
                i++;
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public GameBoard (ArrayList<Player> players) {

        this.players = players;
        publicAchievements = new ArrayList<>();

        this.roundTrack = new RoundTrack();
        this.diceBag = new DiceBag();

        List<PublicAchievementNames> publicAchievementList = Arrays.asList(PublicAchievementNames.values());
        Collections.shuffle(publicAchievementList);
        AbstractCardFactory abstractFactory = new CardFactory();
        for ( int i = 0; i < NUMBER_OF_PUBLIC_ACHIEVEMENTS; i++ ) {
            CardAchievement publicAchievementsFactory = abstractFactory.extractCardAchievement(publicAchievementList.get(i));
            this.publicAchievements.add(publicAchievementsFactory);
        }

        List<Color> privateAchievementsList = Arrays.asList(Color.values());
        Collections.shuffle(privateAchievementsList);
        int i = 0;

        try {
            for (Player p: players) {
                p.setPrivateAchievement(new PrivateAchievement(privateAchievementsList.get(i)));
                i++;
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }
        setChanged();
        notifyObservers(this);
    }

    /**
     * Returns a RoundTrack object representing the current game's round track.
     * @return the round track used in the current game
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Returns a DiceBag object representing the current game's dice bag.
     * @return the dice bag used in the current game
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * Returns an ArrayList containing Player objects which represent the players.
     * @return the players list
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns an ArrayList containing CardAchievements objects which represent the public achievement extracted
     * for the current game.
     * @return the public achievements list
     */
    public ArrayList<CardAchievement> getPublicAchievements() {
        return publicAchievements;
    }

    /**
     * Returns an ArrayList containing Die objects. The ArrayList represent the draft pool used
     * for the current round (updated at the start of every round during the game).
     * @return the draft pool used for the current round
     */
    public ArrayList<Die> getDraftPool() {
         ArrayList draftpoolCopy = new ArrayList<>();
         draftpoolCopy.addAll(this.draftPool);
         return draftpoolCopy;
    }

    /**
     * Allows the user to change the reference for the draftPool attribute, in order to
     * initialize a new draft pool at the start of each round.
     * @param draftPool the new draft pool created at the start of the round
     */
    public void setDraftPool(ArrayList<Die> draftPool) {
        this.draftPool = draftPool;
        setChanged();
        notifyObservers(this);
    }

    /**
     * Allows the user to remove all the Die objects from the draftPool in order to empty it.
     */
    public void emptyDraftPool () {
        if(this.draftPool != null)
            this.draftPool.clear();
        setChanged();
        notifyObservers(this);
    }

    /**
     * Allows the user to remove a Die object from the draftPool.
     * The die argument must specify which Die object the user wants to remove from the draftPool.
     * @param die the die the user wants to remove from the draft pool
     */
    public void removeDieFromDraftPool ( Die die ){
        try {
            this.draftPool.remove(draftPool.indexOf(die));
        }catch (Exception e) {
            System.out.println(e.toString());
        }
        setChanged();
        notifyObservers(this);
    }

    public void addDieToDraftPool ( Die die ){
        this.draftPool.add( die);
    }

    public Die changeDie ( Die die, int dieIndex ) {
        Die myDie = this.draftPool.remove(dieIndex);
        this.draftPool.add( dieIndex , die );
        return myDie;
    }

    public void setCurrentPlayer (Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        setChanged();
        notifyObservers(this);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String toString(){
        StringBuilder bld = new StringBuilder();
        bld.append(roundTrack.toString());
        bld.append("\n");
        bld.append("\nThe dice you can draw are: ");
        for (Die die: draftPool) {
            bld.append(die.toString());
            bld.append(" ");
        }
        bld.append("\n");
        for (Player p: players)
            bld.append(p.getDashboard().toString());
        return bld.toString();
    }

    public ArrayList<Tool> getTools() {
        return this.tools;
    }

    public void setTools(ArrayList<Tool> tools) {
        this.tools=tools;

    }

}