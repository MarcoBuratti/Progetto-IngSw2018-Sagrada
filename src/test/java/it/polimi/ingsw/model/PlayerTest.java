package it.polimi.ingsw.model;

import it.polimi.ingsw.model.achievement.PrivateAchievement;
import it.polimi.ingsw.model.exception.NotEnoughFavourTokensLeft;
import it.polimi.ingsw.model.restriction.ColourRestriction;
import it.polimi.ingsw.model.restriction.NoRestriction;
import it.polimi.ingsw.model.restriction.Restriction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    public void playerTest() throws NotEnoughFavourTokensLeft {
        String nickname = "tester";
        int favourToken = 4;
        Cell[][] matrix = new Cell[1][1];
        Restriction restriction = new NoRestriction();
        matrix[0][0] = new Cell(restriction);
        Dashboard dashboard = new Dashboard(matrix, favourToken);
        PrivateAchievement privateAchievement = new PrivateAchievement(Colour.GREEN);
        Player player = new Player(nickname, dashboard,privateAchievement);
        assertEquals(nickname, player.getNickname());
        assertEquals(favourToken, player.getCurrentFavourToken());
        assertEquals(privateAchievement.toString(), player.getPrivateAchievement().toString());
        assertEquals(dashboard, player.getDashboard());
        player.useToken(true);
        assertEquals(2, player.getCurrentFavourToken());
        player.useToken(false);
        assertEquals(1, player.getCurrentFavourToken());
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(true));
        player.useToken(false);
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(false));
        assertThrows(NotEnoughFavourTokensLeft.class, ()->player.useToken(true));
    }
}