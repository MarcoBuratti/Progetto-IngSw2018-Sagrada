package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotEnoughDiceLeftException;

import java.util.*;

public class Round {
    private static int DRAFT_POOL_CAPACITY;
    private ArrayList<Die> draftPool;
    private Turn currentTurn;

    public Round(List<Player> players) {
        DRAFT_POOL_CAPACITY = (players.size() * 2) + 1;
        this.draftPool = new ArrayList<>();
    }

    public ArrayList<Die> getDraftPool() {
        return draftPool;
    }

    public void initializeDraftPool(GameBoard gameBoard) throws NotEnoughDiceLeftException {
        this.draftPool.addAll(gameBoard.getDiceBag().extractSet(DRAFT_POOL_CAPACITY));
    }

    public void roundManager(List<Player> players, GameBoard gameBoard) {
        ListIterator<Player> iterator = players.listIterator();
        Map<Player, Boolean> secondTurnPlayed = new HashMap<>(players.size());

        while (iterator.hasNext()) {
            this.currentTurn = new Turn(iterator.next(), gameBoard, false,this);
            Thread turn = new Thread(currentTurn);
            turn.start();
            while (!turn.isAlive())
                secondTurnPlayed.put(iterator.next(), currentTurn.isHasSecondTurn());
        }
        while (iterator.hasPrevious()) {
            if (secondTurnPlayed.get(iterator.next())) {
                this.currentTurn = new Turn(iterator.next(), gameBoard, true,this);
                Thread turn = new Thread(currentTurn);
                turn.start();
            }
        }
    }

    public void endRound(GameBoard gameBoard) {
        try {
            gameBoard.getRoundTrack().setNextRound(this.draftPool);
            gameBoard.emptyDraftPool();
        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public void removeDieFromDraftPool( int index ){
        try {
            Die die = this.draftPool.get(index);
            this.draftPool.remove(draftPool.indexOf(die));
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
