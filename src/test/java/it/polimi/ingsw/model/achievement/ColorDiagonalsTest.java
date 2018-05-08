package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Die;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorDiagonalsTest {

    @Test
    void scoreEffect() throws NotValidParametersException {

        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }
        Dashboard dashboard = new Dashboard(matrixScheme);

        Assertions.assertEquals(0, new ColorDiagonals().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,0,new Die(Colour.GREEN));
        dashboard.setDieOnCell(1,1,new Die(Colour.GREEN));
        dashboard.setDieOnCell(2,2,new Die(Colour.YELLOW));
        dashboard.setDieOnCell(3,3,new Die(Colour.YELLOW));

        Assertions.assertEquals(4, new ColorDiagonals().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,3,new Die(Colour.VIOLET));

        Assertions.assertEquals(4, new ColorDiagonals().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,1,new Die(Colour.RED));
        dashboard.setDieOnCell(0,2,new Die(Colour.GREEN));
        dashboard.setDieOnCell(3,4,new Die(Colour.GREEN));
        dashboard.setDieOnCell(0,4,new Die(Colour.BLUE));
        dashboard.setDieOnCell(2,4,new Die(Colour.VIOLET));

        Assertions.assertEquals(5, new ColorDiagonals().scoreEffect(dashboard));

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

        Assertions.assertEquals(11, new ColorDiagonals().scoreEffect(dashboard));
    }
}