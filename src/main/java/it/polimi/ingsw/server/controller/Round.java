package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;

import java.util.*;

public class Round implements Observer {
    private final int draftPoolCapacity;
    private Player currentPlayer;
    private Turn currentTurn;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private ArrayList<RemoteView> remoteViews;
    private RemoteView currentPlayerRemoteView;
    private boolean onePlayerLeft = false;

    /**
     * Creates a Round Object, setting the references to the remote views, to the players and to the game board and also setting the draftPoolCapacity attribute.
     * @param remoteViews a List of RemoteView Objects
     * @param players a List of Player Objects
     * @param gameBoard a GameBoard Object
     */
    public Round( List<RemoteView> remoteViews, List<Player> players, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        draftPoolCapacity = (players.size() * 2) + 1;
        this.players = (ArrayList<Player>) players;
        this.remoteViews = (ArrayList<RemoteView>) remoteViews;
    }

    /**
     * Creates the draft pool associated with the round and sets it on the game board.
     * @throws NotEnoughDiceLeftException if there are not enough dice left in the dice bag
     */
    void initializeDraftPool() throws NotEnoughDiceLeftException {
        ArrayList<Die> draftPool = (ArrayList<Die>) gameBoard.getDiceBag().extractSet(draftPoolCapacity);
        this.gameBoard.setDraftPool(draftPool);
    }

    /**
     * Manages the round, creating turns for players while iterating the list.
     */
    void roundManager() {
        ListIterator<Player> iterator = players.listIterator();

        while (iterator.hasNext() && !onePlayerLeft) {
            currentPlayer = iterator.next();
            this.gameBoard.setCurrentPlayer(currentPlayer);
            createTurn(false);
            endTurn();
        }

        while (iterator.hasPrevious() && !onePlayerLeft) {
            currentPlayer = iterator.previous();
            if (currentPlayer.skipSecondTurn())
                currentPlayer.setSkipSecondTurn(false);
            else {
                this.gameBoard.setCurrentPlayer(currentPlayer);
                createTurn(true);
                endTurn();
            }
        }
    }

    /**
     * Creates a turn for the currentPlayer and communicates to all the players that his turn has started.
     * @param secondTurn a boolean specifying whether it's the player's second turn (in the round) or not
     */
    private void createTurn (boolean secondTurn) {
        currentPlayerRemoteView = this.searchRemoteView( currentPlayer );

        for (RemoteView r: remoteViews)
            if ( ( !r.getPlayer().equals( currentPlayer ) ) && ( r.isOn() ) && ( currentPlayerRemoteView.isOn() ))
                r.send("It's " + currentPlayer.getNickname() + "'s turn. Please wait.");

        if ( currentPlayerRemoteView.isOn() ) {
            currentPlayerRemoteView.send("It's your turn! Please make your move.");
            if ( !onePlayerLeft ) {
                this.currentTurn = new Turn(searchRemoteView(currentPlayer), currentPlayer, gameBoard, secondTurn);
                currentTurn.turnManager();
            }
        }
    }

    /**
     * Manages the end of a turn, communicating to all the players that the current player's turn has ended.
     */
    private void endTurn () {
        for (RemoteView r: remoteViews)
            if ( ( !r.getPlayer().equals( currentPlayer ) ) && ( r.isOn() ) && ( currentPlayerRemoteView.isOn() ))
                r.send(currentPlayer.getNickname() + "'s turn has ended.");

        if ( currentPlayerRemoteView.isOn() )
            currentPlayerRemoteView.send("Your turn has ended.");

    }

    /**
     * Return the RemoteView Object associated with the player.
     * @param player a Player Object representing the player
     * @return a RemoteView Object
     */
    private RemoteView searchRemoteView(Player player) {
        for ( RemoteView remoteView: remoteViews )
            if ( remoteView.getPlayer().getNickname().equals( player.getNickname() ) )
                return remoteView;
        throw new IllegalArgumentException();
    }

    /**
     * Manages the end of the round.
     */
    void endRound() {
        try {
            gameBoard.getRoundTrack().setNextRound(gameBoard.getDraftPool());
            gameBoard.emptyDraftPool();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Manages the end of the round when there's only one player connected due to the other players' disconnection.
     */
    synchronized void onePlayerLeftEnd() {
        this.onePlayerLeft = true;
        if (this.currentTurn != null)
            this.currentTurn.setTurnIsOver();
    }

    /**
     * Returns the currentPlayer attribute.
     * @return a Player Object representing the player who's playing the current turn
     */
    synchronized Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Receive a new PlayerMove from the Controller and sends it to the currentTurn.
     * @param o the Controller
     * @param arg the new PlayerMove
     */
    @Override
    public void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        currentTurn.newMove(playerMove);
    }


}
