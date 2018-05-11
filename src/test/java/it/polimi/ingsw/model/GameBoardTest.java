package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    public void gameBoardTest() throws NotValidValueException {
        Map<String, String> map = new HashMap<>();
        map.put("sergio", "Aurora Sagradis");
        map.put("christian", "Chromatic Splendor");
        map.put("marco", "Fulgor del Cielo");
        GameBoard gameBoard = new GameBoard(map);
        ArrayList<Die> diceStock = new ArrayList<>();
        Die d1, d2, d3;
        d1 = new Die(Color.VIOLET);
        d2 = new Die(Color.YELLOW);
        d3 = new Die(Color.GREEN);
        diceStock.add(d1);
        diceStock.add(d2);
        diceStock.add(d3);
        gameBoard.setDiceStock(diceStock);
        assertNotNull(gameBoard.getDiceBag());
        assertNotNull(gameBoard.getRoundTrack());
        assertEquals(3, gameBoard.getPlayers().size());
        assertEquals("sergio", gameBoard.getPlayers().get(2).getNickname());
        assertEquals("christian", gameBoard.getPlayers().get(0).getNickname());
        assertEquals("marco", gameBoard.getPlayers().get(1).getNickname());
        assertTrue(new Dashboard("Aurora Sagradis").equalsScheme(gameBoard.getPlayers().get(2).getDashboard()));
        assertNotNull(gameBoard.getPlayers().get(0).getPrivateAchievement());
        assertEquals(4, gameBoard.getPlayers().get(0).getCurrentFavourToken());
        assertEquals(3, gameBoard.getPublicAchievements().size());
        assertEquals(diceStock, gameBoard.getDiceStock());
        gameBoard.removeDieFromDiceStock(d2);
        assertEquals(2, gameBoard.getDiceStock().size());
        assertTrue(gameBoard.getDiceStock().contains(d1));
        assertTrue(gameBoard.getDiceStock().contains(d3));
        assertFalse(gameBoard.getDiceStock().contains(d2));
        System.out.println(gameBoard.getPublicAchievements().toString());
    }
}