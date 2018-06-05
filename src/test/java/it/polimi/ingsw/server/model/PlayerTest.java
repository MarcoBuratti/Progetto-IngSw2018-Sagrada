package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotEnoughFavourTokensLeft;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {
    @Test
    public void playerTest() throws NotEnoughFavourTokensLeft, NotValidValueException {

        String nickname = "tester";
        Dashboard dashboard = new Dashboard("Scheme_Test");
        PrivateAchievement privateAchievement = new PrivateAchievement(Color.GREEN);
        Player player = new Player(nickname, dashboard,privateAchievement);
        assertEquals(nickname, player.getNickname());
        assertEquals(3, player.getCurrentFavourToken());
        assertEquals(privateAchievement.toString(), player.getPrivateAchievement().toString());
        assertEquals(dashboard, player.getDashboard());
        player.useToken(true);
        assertEquals(1, player.getCurrentFavourToken());
        player.useToken(false);
        assertEquals(0, player.getCurrentFavourToken());
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(true));
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(false));
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(true));
    }
}