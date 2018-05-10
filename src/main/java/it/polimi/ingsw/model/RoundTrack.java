package it.polimi.ingsw.model;
import it.polimi.ingsw.model.exception.*;

import java.util.List;
import java.util.ArrayList;

public class RoundTrack {
    private static final int NUMBER_OF_ROUNDS = 10;
    private ArrayList[] diceList;

    /**
     * Creates a RoundTrack object which represents the round track on the game board.
     * The round track has as many cells as the number of rounds.
     * At the end of each round, the remaining dice from the dice stock are placed on the cell representing the just ended round.
     * Round track's cells are represented as ArrayLists of Die objects.
     */
    public RoundTrack() {
        diceList = new ArrayList[NUMBER_OF_ROUNDS];
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++)
            diceList[i] = new ArrayList<Die>();
    }

    /**
     * Returns a Die objects ArrayList, representing the list of dice placed on a round track's cell.
     * The argument must specify which cell the user wants to check.
     * @param round the round track's cell that the user wants to check
     * @return an ArrayList containing the dice placed on the selected cell
     * @throws NotValidRoundException whether the user tries to access a not existing round track's cell
     */
    public List getDiceList(int round) throws NotValidRoundException {
        if( round > 0 && round <= NUMBER_OF_ROUNDS)
            return diceList[round-1];
        else throw new NotValidRoundException();
    }

    /**
     * Allows the user to place the dice remaining in the dice stock (at the end of each round) on the selected cell.
     * The argument must specify which cell the user wants to place the dice on.
     * @param mySet the dice set the user wants to place
     * @param round the cell the user wants to place the dice on
     * @throws NotValidRoundException whether the user specifies a not existing round track's cell
     */
    public void setDiceList(ArrayList<Die> mySet, int round) throws NotValidRoundException {
        if ( round > 0 && round <= NUMBER_OF_ROUNDS)
            diceList[round - 1] = mySet;
        else throw new NotValidRoundException();
    }

    /**
     * Returns an int representing the round which is currently played.
     * As the game allows you to put dice on the round's cell only at the end of the round, we're sure that the first empty cell
     * represents the currently played round.
     * @return the currently played round
     * @throws EndedGameException if every round has been already played
     */
    public int getCurrentRound() throws EndedGameException {
        int roundIndex = 0;
        while(roundIndex < NUMBER_OF_ROUNDS) {
            if (this.diceList[roundIndex].isEmpty())
                return roundIndex+1;
            roundIndex++;
        }
        throw new EndedGameException();
    }

    /**
     * Allows the user to place the dice remaining in the dice stock (at the end of the round) on the first empty cell (representing the currently played round).
     * This also marks the end of that round.
     * @param mySet the dice set the user wants to place on the cell
     * @throws NotValidRoundException if the user tries to access the round track after the game is ended (every round already played)
     */
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


    @Override
    public String toString() {
        String myRoundTrack = "Round track:\n\n";
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