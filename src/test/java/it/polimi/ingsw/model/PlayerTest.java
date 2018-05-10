package it.polimi.ingsw.model;

import it.polimi.ingsw.model.achievement.PrivateAchievement;
import it.polimi.ingsw.model.exception.NotEnoughFavourTokensLeft;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.restriction.ColorRestriction;
import it.polimi.ingsw.model.restriction.NoRestriction;
import it.polimi.ingsw.model.restriction.Restriction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void playerTest() throws NotEnoughFavourTokensLeft, NotValidNumberException {

        String nickname = "tester";
        Dashboard dashboard = new Dashboard("Scheme Test");
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