package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotEnoughFavourTokensLeft;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {
    @Test
    public void playerTest() throws NotEnoughFavourTokensLeft, NotValidValueException {

        String nickname = "tester";
        PrivateAchievement privateAchievement = new PrivateAchievement(Color.GREEN);
        Player player = new Player(nickname, null);
        player.setDashboard("Scheme_Test");
        player.setPrivateAchievement(privateAchievement);
        assertEquals(nickname, player.getNickname());
        assertEquals(3, player.getCurrentFavourToken());
        assertEquals(privateAchievement.toString(), player.getPrivateAchievement().toString());
        assertTrue(player.getDashboard().equalsScheme(new Dashboard("Scheme_Test")));
        player.useToken(true);
        assertEquals(1, player.getCurrentFavourToken());
        player.useToken(false);
        assertEquals(0, player.getCurrentFavourToken());
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(true));
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(false));
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(true));
    }
}