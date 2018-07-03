package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.RemoteView;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;

import java.util.*;

public class Round implements Observer {
    private int draftPoolCapacity;
    private Player currentPlayer;
    private Turn currentTurn;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private ArrayList<RemoteView> remoteViews;
    private RemoteView currentPlayerRemoteView;
    private boolean onePlayerLeft = false;

    public Round(List<RemoteView> remoteViews, List<Player> players, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        draftPoolCapacity = (players.size() * 2) + 1;
        this.players = new ArrayList<>(players);
        this.remoteViews = new ArrayList<>(remoteViews);
    }

    void initializeDraftPool() throws NotEnoughDiceLeftException {
        ArrayList<Die> draftPool = (ArrayList<Die>) gameBoard.getDiceBag().extractSet(draftPoolCapacity);
        this.gameBoard.setDraftPool(draftPool);
    }

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

    private void createTurn (boolean bool) {
        currentPlayerRemoteView = this.searchRemoteView( currentPlayer );

        for (RemoteView r: remoteViews)
            if ( ( !r.getPlayer().equals( currentPlayer ) ) && ( r.isOn() ) && ( currentPlayerRemoteView.isOn() ))
                r.send("It's " + currentPlayer.getNickname() + "'s turn. Please wait.");

        if ( currentPlayerRemoteView.isOn() ) {
            currentPlayerRemoteView.send("It's your turn! Please make your move.");
            if ( !onePlayerLeft ) {
                this.currentTurn = new Turn(searchRemoteView(currentPlayer), currentPlayer, gameBoard, bool);
                currentTurn.turnManager();
            }
        }
    }

    private void endTurn () {
        for (RemoteView r: remoteViews)
            if ( ( !r.getPlayer().equals( currentPlayer ) ) && ( r.isOn() ) && ( currentPlayerRemoteView.isOn() ))
                r.send(currentPlayer.getNickname() + "'s turn has ended.");

        if ( currentPlayerRemoteView.isOn() )
            currentPlayerRemoteView.send("Your turn has ended.");

    }

    private RemoteView searchRemoteView(Player player) {
        for ( RemoteView remoteView: remoteViews )
            if ( remoteView.getPlayer().getNickname().equals( player.getNickname() ) )
                return remoteView;
        throw new IllegalArgumentException();
    }

    void endRound() {
        try {
            gameBoard.getRoundTrack().setNextRound(gameBoard.getDraftPool());
            gameBoard.emptyDraftPool();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    synchronized void onePlayerLeftEnd() {
        this.onePlayerLeft = true;
        if (this.currentTurn != null)
            this.currentTurn.onePlayerLeft();
    }

    synchronized Player getCurrentPlayer() {
        return currentPlayer;
    }


    @Override
    public void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        currentTurn.newMove(playerMove);
    }


}
