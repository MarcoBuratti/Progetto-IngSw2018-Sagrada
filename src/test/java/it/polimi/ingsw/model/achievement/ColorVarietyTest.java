package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ColorVarietyTest {

    @Test
    void scoreEffect() throws NotValidParametersException, OccupiedCellException {

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

        dashboard.setDieOnCell(0,1,new Die(Colour.RED));
        dashboard.setDieOnCell(0,2,new Die(Colour.YELLOW));
        dashboard.setDieOnCell(3,4,new Die(Colour.GREEN));
        dashboard.setDieOnCell(0,4,new Die(Colour.BLUE));
        dashboard.setDieOnCell(2,4,new Die(Colour.VIOLET));

        Assertions.assertEquals(8, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(1,0,new Die(Colour.RED));
        dashboard.setDieOnCell(1,2,new Die(Colour.YELLOW));
        dashboard.setDieOnCell(1,3,new Die(Colour.GREEN));
        dashboard.setDieOnCell(1,4,new Die(Colour.BLUE));
        dashboard.setDieOnCell(2,0,new Die(Colour.VIOLET));
        dashboard.setDieOnCell(2,1,new Die(Colour.RED));
        dashboard.setDieOnCell(2,3,new Die(Colour.YELLOW));
        dashboard.setDieOnCell(3,0,new Die(Colour.GREEN));
        dashboard.setDieOnCell(3,1,new Die(Colour.BLUE));
        dashboard.setDieOnCell(3,2,new Die(Colour.VIOLET));

        Assertions.assertEquals(16, new ColorVariety().scoreEffect(dashboard));
    }

}