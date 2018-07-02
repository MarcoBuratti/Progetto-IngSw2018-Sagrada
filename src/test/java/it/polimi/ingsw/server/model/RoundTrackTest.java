package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.EndedGameException;
import it.polimi.ingsw.server.model.exception.NotValidRoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoundTrackTest {
    public static final int NUMBER_OF_ROUNDS = 10;

    @Test
    public void roundTrackTest() throws NotValidRoundException, EndedGameException {
        RoundTrack roundTrack = new RoundTrack();
        for (int i = 1; i <= NUMBER_OF_ROUNDS; i++)
            assertTrue(roundTrack.getDiceList(i).isEmpty());
        Die d1, d2;
        d1 = new Die(Color.BLUE);
        d2 = new Die(Color.RED);
        ArrayList<Die> diceList = new ArrayList<>();
        diceList.add(d1);
        diceList.add(d2);
        assertThrows(NotValidRoundException.class, ()->roundTrack.setDiceList(diceList, 0));
        roundTrack.setDiceList(diceList, 1);
        assertEquals(diceList, roundTrack.getDiceList(1));
        Die d3 = new Die(Color.YELLOW);
        ArrayList<Die> diceList2 = new ArrayList<>();
        diceList2.add(d3);
        assertEquals(2, roundTrack.getCurrentRound());
        roundTrack.setNextRound(diceList2);
        ArrayList<Die> diceList3 = new ArrayList<>();
        roundTrack.setNextRound(diceList3);
        assertTrue(diceList3.isEmpty());
        assertEquals(3, roundTrack.getCurrentRound());
        Die d4 = new Die(Color.VIOLET);
        diceList3.add(d4);
        roundTrack.setDiceList(diceList3, 3);
        assertEquals(4, roundTrack.getCurrentRound());
        for (int i = 0; i < NUMBER_OF_ROUNDS-3 ; i++)
            roundTrack.setNextRound(diceList2);
        assertThrows(EndedGameException.class, ()->roundTrack.getCurrentRound());
        assertThrows(NotValidRoundException.class, ()->roundTrack.setNextRound(diceList));
        assertThrows(NotValidRoundException.class, ()->roundTrack.setDiceList(diceList2, 11));
        System.out.println(roundTrack.toString());
    }

}