package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.achievement.CardAchievement;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;
import javafx.util.Pair;

import java.util.*;

public class Controller extends Observable implements Observer {
    private static final int NUMBER_OF_ROUNDS = 1;
    private static final String PLACE_DIE = "PlaceDie";
    private static final String USE_TOOL = "UseTool";
    private static final String GO_THROUGH = "GoThrough";
    private Round currentRound;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private ArrayList<RemoteView> remoteViews;
    private boolean gameEnded;
    private boolean onePlayerLeft = false;

    /**
     * Creates a new Controller Object, also creating a new GameBoard Object and setting it as gameBoard attribute.
     * @param game the Game Object that calls the constructor ( players is set using game's getPlayers method )
     */
    public Controller(Game game) {
        gameBoard = new GameBoard(game.getPlayers());
        players = (ArrayList<Player>) gameBoard.getPlayers();
    }

    /**
     * Sets the remoteView attribute and adds the controller as Observer of all the RemoteView Objects contained in remoteViews.
     * @param game the Game Object that has the references to the Remote View Objects
     */
    public void setRemoteViews(Game game) {
        remoteViews = (ArrayList<RemoteView>) game.getRemoteViews();
        for (RemoteView r : remoteViews)
            r.addObserver(this);
    }

    /**
     * Starts the game and returns the winner's nickname.
     * @return a String
     */
    public String startGame() {

        for (int i = 0; i < NUMBER_OF_ROUNDS && !onePlayerLeft ; i++) {
            currentRound = new Round( remoteViews, players, gameBoard);
            this.addObserver(currentRound);
            try {
                currentRound.initializeDraftPool();
            } catch (NotEnoughDiceLeftException e) {
                e.printStackTrace();
            }
            currentRound.roundManager();
            if(!onePlayerLeft)
                 currentRound.endRound();
            Collections.rotate(players, players.size()-1);
        }

        ArrayList< Pair < RemoteView, Integer > > finalScores = this.calculateFinalScores();
        Player winner = finalScores.get(0).getKey().getPlayer();
        if (!onePlayerLeft) {
            endGame(finalScores, winner);
        }
        return winner.getNickname();
    }

    /**
     * Returns the gameBoard attribute.
     * @return a GameBoard Object representing the game board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Manages the end of the game, communicating to the players who is the winner and the score of each player.
     * @param finalScores an ArrayList of Pairs containing the score of each player
     * @param winner a Player Object representing the player who has won the game
     */
    private void endGame (ArrayList< Pair < RemoteView, Integer > > finalScores, Player winner) {
        for (Pair p : finalScores) {
            RemoteView remoteView = (RemoteView) p.getKey();
            Player player = remoteView.getPlayer();

            if ( remoteView.isOn() ) {
                if (player.getNickname().equals(winner.getNickname()))
                    remoteView.send("You win!");
                else
                    remoteView.send("You lose!");

                for (Pair pair : finalScores) {
                    RemoteView r = (RemoteView) pair.getKey();
                    Player otherPlayer = r.getPlayer();
                    remoteView.send("Player: " + otherPlayer.getNickname() + " , Score: " + pair.getValue());
                }
            }
        }
        setGameEnded();
    }

    /**
     * Returns an ArrayList of Pair Objects. Every Pair contains a RemoteView and an Integer and associates a player to his final score.
     * @return an ArrayList of Pair Objects
     */
    private ArrayList< Pair< RemoteView, Integer > > calculateFinalScores() {

        ArrayList< Pair< RemoteView , Integer > > finalScores = new ArrayList<>();
        Collections.rotate(players, 1);

        for (Player p : this.players) {

            RemoteView remoteView = searchRemoteView( p );
            Pair< RemoteView, Integer > pair = new Pair<>( remoteView , calculateFinalScorePlayer(p));
            boolean added = false;

            for (int i = 0; i < finalScores.size() && !added; i++) {
                if (compare(finalScores.get(i), pair)) {
                    finalScores.add(i, pair);
                    added = true;
                }
            }

            if (!added) {
                finalScores.add(pair);
            }
        }

        return finalScores;
    }

    /**
     * Returns the RemoteView associated with the player specified as argument.
     * @param player a Player Object representing a player
     * @return a RemoteView Object associated with player
     */
    private RemoteView searchRemoteView ( Player player ) {
        for ( RemoteView remoteView: remoteViews )
            if ( remoteView.getPlayer().getNickname().equals( player.getNickname() ) )
                return remoteView;
        throw new IllegalArgumentException();
    }

    /**
     * Compares two players' scores and returns true if the player associated with the first Pair Object wins.
     * @param pair1 the Pair Object associated with the first player
     * @param pair2 the Pair Object associated with the second player
     * @return a boolean
     */
    private boolean compare( Pair< RemoteView, Integer > pair1, Pair< RemoteView, Integer > pair2){

        Player p1 = pair1.getKey().getPlayer();
        Player p2 = pair2.getKey().getPlayer();
        if (pair1.getValue() < pair2.getValue())
            return true;
        else if (pair1.getValue().equals(pair2.getValue()) &&
                p1.getPrivateAchievement().scoreEffect(p1.getDashboard()) < p2.getPrivateAchievement().scoreEffect(p2.getDashboard()))
                return true;
        else if (p1.getPrivateAchievement().scoreEffect(p1.getDashboard()) == p2.getPrivateAchievement().scoreEffect(p2.getDashboard()) &&
            p1.getCurrentFavourToken() < p2.getCurrentFavourToken())
                return true;
        else return p1.getCurrentFavourToken() == p2.getCurrentFavourToken() &&
                    this.players.indexOf(p1) > this.players.indexOf(p2);


    }

    /**
     * Calculate the final score associated with the player specified as a Player Object arguemnt.
     * @param player the Player Object associated with the considered player
     * @return an int representing the score
     */
    private int calculateFinalScorePlayer(Player player) {
        int score = 0;
        score += player.getPrivateAchievement().scoreEffect(player.getDashboard());
        for (CardAchievement c : gameBoard.getPublicAchievements()) {
            score += c.scoreEffect(player.getDashboard());

        }
        score += player.getCurrentFavourToken();
        score -= player.getDashboard().emptyCells();
        return score;
    }

    /**
     * Manages the end of the game when some of the players have disconnected and only one player is connected.
     */
    public synchronized void onePlayerLeftEnd () {
        this.onePlayerLeft = true;
        if(this.currentRound != null)
            this.currentRound.onePlayerLeftEnd();
    }

    /**
     * Manages a PlayerMove Object associated with a placement move, checking if the dieIndex parameter is present and if its value is acceptable.
     * @param playerMove a PlayerMove Object representing the move the player is trying to make
     * @param o an Observable Object: the RemoteView associated with the player
     */
    private void placementMoveHandler(PlayerMove playerMove, Observable o) {
        Optional<Integer> dieIndex = playerMove.getIndexDie();
        if (dieIndex.isPresent()) {
            Integer dieIndexValue = dieIndex.get();
            if (dieIndexValue < gameBoard.getDraftPool().size()) {
                setChanged();
                notifyObservers(playerMove);
            } else {
                RemoteView remoteView = (RemoteView) o;
                remoteView.incorrectMove();
            }
        } else {
            RemoteView remoteView = (RemoteView) o;
            remoteView.incorrectMove();
        }
    }

    /**
     * Manages a PlayerMove Object associated with a tool move, checking if the toolIndex parameter is present.
     * @param playerMove a PlayerMove Object representing the move the player is trying to make
     * @param o an Observable Object: the RemoteView associated with the player
     */
    private void toolMoveHandler (PlayerMove playerMove, Observable o) {

        Optional<Integer> toolIndex = playerMove.getExtractedToolIndex();
        if (toolIndex.isPresent()) {
            setChanged();
            notifyObservers(playerMove);
        } else {
            RemoteView remoteView = (RemoteView) o;
            remoteView.incorrectMove();
        }
    }

    /**
     * Manages the PlayerMove received from the RemoteViews.
     * @param o the RemoteView the PlayerMove was sent by
     * @param arg the PlayerMove sent by the RemoteView
     */
    @Override
    public void update(Observable o, Object arg) {

        PlayerMove playerMove = (PlayerMove) arg;

        if ( playerMove.getPlayerNickname().equals(currentRound.getCurrentPlayer().getNickname()) ) {


            switch (playerMove.getMoveType()) {
                case PLACE_DIE:
                    placementMoveHandler(playerMove, o);
                    break;

                case USE_TOOL:
                    toolMoveHandler(playerMove, o);
                    break;

                case GO_THROUGH:
                default:
                    setChanged();
                    notifyObservers(playerMove);
                    break;
            }
        }
        else {
            RemoteView remoteView = (RemoteView) o;
            remoteView.notYourTurn();
        }
    }

    /**
     * Returns the gameEnded attribute.
     * @return an int specifying whether the game has ended or not
     */
    public synchronized boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Allows the user to set the gameEnded attribute as true.
     */
    private synchronized void setGameEnded() {
        this.gameEnded = true;
    }
}
