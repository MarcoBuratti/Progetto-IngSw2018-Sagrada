package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;

import java.util.*;

public class Round {
    private static int DRAFT_POOL_CAPACITY;
    private ArrayList<Die> draftPool;
    private Turn currentTurn;

    public Round(List<Player> players) {
        DRAFT_POOL_CAPACITY = (players.size() * 2) + 1;
        this.draftPool = new ArrayList<>(DRAFT_POOL_CAPACITY);
    }

    public void initializeDraftPool(List<Player> players, GameBoard gameBoard) {
        for (int i = 0; i < DRAFT_POOL_CAPACITY; i++)
            draftPool.add(gameBoard.getDiceBag().extract());
    }

    public void roundManager(List<Player> players, GameBoard gameBoard) {
        ListIterator<Player> iterator = players.listIterator();
        Map<Player, Boolean> secondTurnPlayed = new HashMap<>(players.size());

        while (iterator.hasNext()) {
            this.currentTurn = new Turn(iterator.next(), gameBoard, false);
            Thread turn = new Thread(currentTurn);
            turn.start();
            while (!turn.isAlive())
                secondTurnPlayed.put(iterator.next(), currentTurn.isHasSecondTurn());
        }
        while (iterator.hasPrevious()) {
            if (secondTurnPlayed.get(iterator.next())) {
                this.currentTurn = new Turn(iterator.next(), gameBoard, true);
                Thread turn = new Thread(currentTurn);
                turn.start();
            }
        }
    }

    public void endRound(GameBoard gameBoard) {
        try {
            gameBoard.getRoundTrack().setNextRound(this.draftPool);
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }
}
