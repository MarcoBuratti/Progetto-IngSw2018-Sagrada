package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void playerTest() throws NotValidValueException {

        String nickname = "tester";
        PrivateAchievement privateAchievement = new PrivateAchievement(Color.GREEN);
        Player player = new Player(nickname);
        player.setDashboard("Scheme_Test");
        player.setPrivateAchievement(privateAchievement);
        assertEquals(nickname, player.getNickname());
        assertEquals(3, player.getCurrentFavourToken());
        assertEquals(privateAchievement.toString(), player.getPrivateAchievement().toString());
        assertTrue(player.getDashboard().equalsScheme(new Dashboard("Scheme_Test")));
        assertTrue(player.hasEnoughToken(false));
        assertTrue(player.hasEnoughToken(true));
        player.useToken(true);
        assertTrue(player.hasEnoughToken(false));
        assertFalse(player.hasEnoughToken(true));
        player.useToken(false);
        assertEquals(0, player.getCurrentFavourToken());
    }
}