package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.EndedGameException;
import it.polimi.ingsw.model.exception.NotValidRoundException;

import java.util.*;

public class Round{
    private ArrayList<Die> draftPool;
    private static int DRAFT_POOL_CAPACITY;

    public Round(List<Player> players){
        DRAFT_POOL_CAPACITY = (players.size() * 2 ) + 1;
        this.draftPool= new ArrayList<>(DRAFT_POOL_CAPACITY);
    }

    public void initializeDraftPool(List<Player> players,GameBoard gameBoard) {
        for (int i = 0; i < DRAFT_POOL_CAPACITY; i++)
            draftPool.add(gameBoard.getDiceBag().extract());
    }

    public void roundManager(List<Player> players){
        ListIterator<Player> iterator = players.listIterator();
        Map <Player,Boolean> secondTurnPlayed = new HashMap<>(players.size());


        while(iterator.hasNext()){




        secondTurnPlayed.put(iterator.next(),true);
        }
        while (iterator.hasPrevious()&&secondTurnPlayed.get(iterator.next())){

        }
    }

   public void endRound (GameBoard gameBoard) throws NotValidRoundException, EndedGameException {
        try {
            gameBoard.getRoundTrack().setNextRound(this.draftPool);
        }catch (Exception e){
            System.out.println(e.toString());
        }



   }
}
