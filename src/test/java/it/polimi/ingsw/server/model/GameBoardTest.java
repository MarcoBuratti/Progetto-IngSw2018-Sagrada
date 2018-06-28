package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    @Test
    public void gameBoardTest() throws NotValidValueException {
        ArrayList<Player> playersList = new ArrayList<>();
        Player player;
        player = new Player ( "christian");
        player.setDashboard("Chromatic_Splendor");
        playersList.add( player );
        player = new Player ( "marco");
        player.setDashboard("Fulgor_del_Cielo");
        playersList.add( player );
        player = new Player ( "sergio");
        player.setDashboard("Aurora_Sagradis");
        playersList.add( player );
        GameBoard gameBoard = new GameBoard(playersList);
        ArrayList<Die> draftPool = new ArrayList<>();
        Die d1, d2, d3;
        d1 = new Die(Color.VIOLET);
        d2 = new Die(Color.YELLOW);
        d3 = new Die(Color.GREEN);
        draftPool.add(d1);
        draftPool.add(d2);
        draftPool.add(d3);
        gameBoard.setDraftPool(draftPool);
        assertNotNull(gameBoard.getDiceBag());
        assertNotNull(gameBoard.getRoundTrack());
        assertEquals(3, gameBoard.getPlayers().size());
        assertEquals("sergio", gameBoard.getPlayers().get(2).getNickname());
        assertEquals("christian", gameBoard.getPlayers().get(0).getNickname());
        assertEquals("marco", gameBoard.getPlayers().get(1).getNickname());
        assertTrue(new Dashboard("Aurora_Sagradis").equalsScheme(gameBoard.getPlayers().get(2).getDashboard()));
        assertTrue(new Dashboard("Chromatic_Splendor").equalsScheme(gameBoard.getPlayers().get(0).getDashboard()));
        assertTrue(new Dashboard("Fulgor_del_Cielo").equalsScheme(gameBoard.getPlayers().get(1).getDashboard()));
        assertNotNull(gameBoard.getPlayers().get(0).getPrivateAchievement());
        assertEquals(4, gameBoard.getPlayers().get(0).getCurrentFavourToken());
        assertEquals(3, gameBoard.getPublicAchievements().size());
        assertEquals(draftPool, gameBoard.getDraftPool());
        gameBoard.removeDieFromDraftPool(d2);
        assertEquals(2, gameBoard.getDraftPool().size());
        assertTrue(gameBoard.getDraftPool().contains(d1));
        assertTrue(gameBoard.getDraftPool().contains(d3));
        assertFalse(gameBoard.getDraftPool().contains(d2));
        System.out.println(gameBoard.getPublicAchievements().toString());
    }
}