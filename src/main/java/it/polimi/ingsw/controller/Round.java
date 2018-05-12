package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotValidRoundException;

import java.util.*;

public class Round{

    private ArrayList<Die> draftPool;

    public Round(List<Player> players){

        this.draftPool= new ArrayList<>(players.size()*2+1);
    }

    public void initializeDraftPool(List<Player> players,GameBoard gameBoard) {
        for (int i = 0; i < players.size() * 2 + 1; i++)
            draftPool.add(gameBoard.getDiceBag().extract());
    }

    public void roundManager(List<Player> players){
        ListIterator<Player> iterator = players.listIterator();
        Map <Player,Boolean> secondTurnPlayer = new HashMap<>(players.size());


        while(iterator.hasNext()){




        secondTurnPlayer.put(iterator.next(),true);
        }
        while (iterator.hasPrevious()&&secondTurnPlayer.get(iterator.next())){

        }
    }

   public void addDraftPooltoRoundTrack(GameBoard gameBoard,int round) throws NotValidRoundException {

        gameBoard.getRoundTrack().setDiceList(this.draftPool,round-1);



   }
}
