package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotEnoughDiceLeftException;

import java.util.*;

public class Round {
    private static int DRAFT_POOL_CAPACITY;
    private Turn currentTurn;
    private GameBoard gameBoard;

    public Round(List<Player> players,GameBoard gameBoard) {
        this.gameBoard=gameBoard;
        DRAFT_POOL_CAPACITY = (players.size() * 2) + 1;
    }

    public void initializeDraftPool() throws NotEnoughDiceLeftException {
        ArrayList<Die> draftPool = new ArrayList<>();
        draftPool.addAll(gameBoard.getDiceBag().extractSet(DRAFT_POOL_CAPACITY));
        this.gameBoard.setDraftPool(draftPool);
    }

    public void roundManager(List<Player> players) {
        ListIterator<Player> iterator = players.listIterator();
        Map<Player, Boolean> secondTurnPlayed = new HashMap<>(players.size());

        while (iterator.hasNext()) {
            Player currentPlayer = iterator.next();
            //this.gameBoard.getModelView().setCurrentPlayer(currentPlayer.getNickname());
            this.currentTurn = new Turn(currentPlayer, gameBoard, false,this);
            currentTurn.turnManager();
            secondTurnPlayed.put(currentPlayer, currentTurn.isHasSecondTurn());
        }
        while (iterator.hasPrevious()) {
            Player currentPlayer = iterator.previous();
            if (secondTurnPlayed.get(currentPlayer)) {
                //this.gameBoard.getModelView().setCurrentPlayer(currentPlayer.getNickname());
                this.currentTurn = new Turn(currentPlayer, gameBoard, true,this);
                currentTurn.turnManager();
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
    public Turn getCurrentTurn() {
        return currentTurn;
    }

}
