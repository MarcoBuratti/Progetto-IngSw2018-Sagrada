package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.EndedGameException;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;

import java.util.ArrayList;
import java.util.List;

public class RoundTrack {
    private static final int NUMBER_OF_ROUNDS = 10;
    private ArrayList<ArrayList<Die>> diceList;

    /**
     * Creates a RoundTrack object which represents the round track on the game board.
     * The round track has as many cells as the number of rounds.
     * At the end of each round, the remaining dice from the dice stock are placed on the cell representing the just ended round.
     * Round track's cells are represented as ArrayLists of Die objects.
     */
    public RoundTrack() {
        diceList = new ArrayList<>( NUMBER_OF_ROUNDS );
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++)
            diceList.add(new ArrayList<>());
    }

    /**
     * Returns a Die objects ArrayList, representing the list of dice placed on a round track's cell.
     * The argument must specify which cell the user wants to check.
     *
     * @param round the round track's cell that the user wants to check
     * @return an ArrayList containing the dice placed on the selected cell
     * @throws NotValidRoundException whether the user tries to access a not existing round track's cell
     */
    public List<Die> getDiceList ( int round ) throws NotValidRoundException {
        if (round > 0 && round <= NUMBER_OF_ROUNDS)
            return diceList.get(round - 1);
        else throw new NotValidRoundException();
    }

    /**
     * Allows the user to place the dice remaining in the dice stock (at the end of each round) on the selected cell.
     * The argument must specify which cell the user wants to place the dice on.
     *
     * @param mySet the dice set the user wants to place
     * @param round the cell the user wants to place the dice on
     * @throws NotValidRoundException whether the user specifies a not existing round track's cell
     */
    public void setDiceList( List<Die> mySet, int round) throws NotValidRoundException {
        if (round > 0 && round <= NUMBER_OF_ROUNDS)
            diceList.set( round-1 , new ArrayList<>(mySet) );
        else throw new NotValidRoundException();
    }

    /**
     * Returns an int representing the round which is currently played.
     * As the game allows you to put dice on the round's cell only at the end of the round, we're sure that the first empty cell
     * represents the currently played round.
     *
     * @return the currently played round
     * @throws EndedGameException if every round has been already played
     */
    public int getCurrentRound() throws EndedGameException {
        int roundIndex = 0;
        while (roundIndex < NUMBER_OF_ROUNDS) {
            if (this.diceList.get(roundIndex).isEmpty())
                return roundIndex + 1;
            roundIndex++;
        }
        throw new EndedGameException();
    }

    /**
     * Allows the user to place the dice remaining in the dice stock (at the end of the round) on the first empty cell
     * (representing the currently played round).
     * This also marks the end of that round.
     *
     * @param mySet the dice set the user wants to place on the cell
     * @throws NotValidRoundException if the user tries to access the round track after the game is ended (every round already played)
     */
    public void setNextRound( List<Die> mySet) throws NotValidRoundException {
        int currentRound = 0;
        try {
            currentRound = this.getCurrentRound();
        } catch (EndedGameException e) {
            System.out.println(e.toString());
        }
        if (currentRound > 0 && currentRound <= NUMBER_OF_ROUNDS)
            this.setDiceList(mySet, currentRound);
        else throw new NotValidRoundException();
    }

    /**
     * Checks whether there is a die on the round track having the color chosen
     * as argument by the user.
     *
     * @param color the color of which the user wants to know whether there is a die on the round track or not
     * @return a Boolean object specifying if there is a die having that color or not
     */
    public Boolean isColorOnRoundTrack(Color color) {
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++)
            for (Die d : diceList.get(i)) {
                if (d.getColor().equals(color))
                    return true;
            }
        return false;
    }

    /**
     * Allows the user to replace a die from the roundTrack with another one.
     * @param die the die the user wants to put in the draftPool
     * @param round the round cell the user wants to get the die from
     * @param dieIndex the index of the die the user wants to get
     */
    public void changeDie(Die die, int round, int dieIndex) {
        this.diceList.get(round - 1).set(dieIndex, die);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String myRoundTrack = "Round track-";
        StringBuilder bld = new StringBuilder();
        int i;
        for (i = 0; i < NUMBER_OF_ROUNDS && !diceList.get(i).isEmpty(); i++) {
            bld.append("Round ");
            bld.append(i + 1);
            bld.append("   : ");
            for (Die die : diceList.get(i)) {
                bld.append(die.toString()).append(" ");
            }
            bld.append("!");
        }
        if (i == 0) myRoundTrack += " is empty";
        return myRoundTrack + bld.toString();
    }
}