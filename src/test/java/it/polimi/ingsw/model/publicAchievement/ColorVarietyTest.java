package it.polimi.ingsw.model.publicAchievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ColorVarietyTest {

    @Test
    void scoreEffect() {

        Cell[][] matrixScheme = new Cell[4][5];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }

        Dashboard dashboard = new Dashboard(matrixScheme);
        Assertions.assertEquals(0, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,0,new Die(Colour.GREEN));
        dashboard.setDieOnCell(1,1,new Die(Colour.BLUE));
        dashboard.setDieOnCell(2,2,new Die(Colour.YELLOW));
        dashboard.setDieOnCell(3,3,new Die(Colour.RED));

        Assertions.assertEquals(0, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,3,new Die(Colour.VIOLET));

        Assertions.assertEquals(4, new ColorVariety().scoreEffect(dashboard));

    }
}