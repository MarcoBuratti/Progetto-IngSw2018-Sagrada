package it.polimi.ingsw.model;
import it.polimi.ingsw.model.exception.*;

import java.util.List;
import java.util.ArrayList;

public class RoundTrack {
    private static final int NUMBER_OF_ROUNDS = 10;
    private ArrayList[] diceList;

    public RoundTrack() {
        diceList = new ArrayList[NUMBER_OF_ROUNDS];
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++)
            diceList[i] = new ArrayList<Die>();
    }

    public List getDiceList(int round) throws NotValidRoundException {
        if( round > 0 && round <= NUMBER_OF_ROUNDS)
            return diceList[round-1];
        else throw new NotValidRoundException();
    }

    public void setDiceList(ArrayList<Die> mySet, int round) throws NotValidRoundException {
        if ( round > 0 && round <= NUMBER_OF_ROUNDS)
            diceList[round - 1] = mySet;
        else throw new NotValidRoundException();
    }

    public void setNextRound(ArrayList<Die> mySet) throws NotValidRoundException {
        int currentRound = 0;
        try {
            currentRound = this.getCurrentRound();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        if ( currentRound > 0 && currentRound <= NUMBER_OF_ROUNDS )
            this.setDiceList(mySet, currentRound);
        else throw new NotValidRoundException();
    }

    public int getCurrentRound() throws EndedGameException {
        int roundIndex = 0;
        while(roundIndex < NUMBER_OF_ROUNDS) {
            if (this.diceList[roundIndex].isEmpty())
                return roundIndex+1;
            roundIndex++;
        }
        throw new EndedGameException();
    }

    @Override
    public String toString() {
        String myRoundTrack = "Roundtrack:\n\n";
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            bld.append("Round ");
            bld.append(i + 1);
            bld.append(" :\n");
            for (Object die : diceList[i])
                bld.append(die.toString());
        }
        return myRoundTrack + bld.toString();
    }
}