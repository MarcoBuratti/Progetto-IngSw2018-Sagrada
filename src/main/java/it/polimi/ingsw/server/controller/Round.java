package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotEnoughDiceLeftException;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

public class Round implements Observer {
    private int DRAFT_POOL_CAPACITY;
    private Player currentPlayer;
    private Turn currentTurn;
    private GameBoard gameBoard;
    private ArrayList<Player> players;

    public Round(ArrayList<Player> players, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        DRAFT_POOL_CAPACITY = (players.size() * 2) + 1;
        this.players = players;
    }

    public void initializeDraftPool() throws NotEnoughDiceLeftException {
        ArrayList<Die> draftPool = new ArrayList<>();
        draftPool.addAll(gameBoard.getDiceBag().extractSet(DRAFT_POOL_CAPACITY));
        this.gameBoard.setDraftPool(draftPool);
    }

    public void roundManager() {
        ListIterator<Player> iterator = players.listIterator();

        while (iterator.hasNext()) {
            currentPlayer = iterator.next();
            this.gameBoard.setCurrentPlayer(currentPlayer);
            if (currentPlayer.getServerInterface() != null) {
                this.currentTurn = new Turn(currentPlayer, gameBoard, false);
                currentTurn.turnManager();
            }

        }

        while (iterator.hasPrevious()) {
            currentPlayer = iterator.previous();
            if (currentPlayer.skipSecondTurn())
                currentPlayer.setSkipSecondTurn(false);
            else {
                this.gameBoard.setCurrentPlayer(currentPlayer);
                if (currentPlayer.getServerInterface() != null) {
                    this.currentTurn = new Turn(currentPlayer, gameBoard, true);
                    currentTurn.turnManager();
                }
            }
        }
    }

    public void endRound() {
        try {
            gameBoard.getRoundTrack().setNextRound(gameBoard.getDraftPool());
            gameBoard.emptyDraftPool();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public synchronized Player getCurrentPlayer() { return currentPlayer; }

    public synchronized Turn getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        currentTurn.newMove(playerMove);
    }


}
