package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.achievement.Shades;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShadesTest {

    @Test
    void scoreEffect() throws NotValidParametersException, NotValidValueException, OccupiedCellException {

        Cell[][] matrixScheme = new Cell[4][5];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }

        Dashboard dashboard = new Dashboard("Scheme_test");
        Assertions.assertEquals(0, new Shades(1,2).scoreEffect(dashboard));
        Assertions.assertEquals(0, new Shades(5,6).scoreEffect(dashboard));


        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1,0,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2,0,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(3,3,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[3][3].getDie().setNumber(1);

        Assertions.assertEquals(2, new Shades(1,2).scoreEffect(dashboard));
        Assertions.assertEquals(0, new Shades(3,4).scoreEffect(dashboard));

        dashboard.setDieOnCell(3,0,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[3][0].getDie().setNumber(3);
        dashboard.setDieOnCell(1,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[1][1].getDie().setNumber(1);
        dashboard.setDieOnCell(2,2,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][2].getDie().setNumber(6);
        dashboard.setDieOnCell(3,4,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[3][4].getDie().setNumber(6);
        dashboard.setDieOnCell(0,4,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[0][4].getDie().setNumber(6);
        dashboard.setDieOnCell(2,4,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[2][4].getDie().setNumber(5);
        dashboard.setDieOnCell(1,4,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][4].getDie().setNumber(2);

        Assertions.assertEquals(4, new Shades(5,6).scoreEffect(dashboard));
        Assertions.assertEquals(4, new Shades(1,2).scoreEffect(dashboard));
        Assertions.assertEquals(2, new Shades(3,4).scoreEffect(dashboard));

        dashboard.setDieOnCell(0,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[0][1].getDie().setNumber(6);
        dashboard.setDieOnCell(1,2,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[1][2].getDie().setNumber(3);
        dashboard.setDieOnCell(1,3,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[1][3].getDie().setNumber(1);
        dashboard.setDieOnCell(0,2,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[0][2].getDie().setNumber(3);
        dashboard.setDieOnCell(2,1,new Die(Color.RED));
        dashboard.getMatrixScheme()[2][1].getDie().setNumber(4);
        dashboard.setDieOnCell(2,3,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[2][3].getDie().setNumber(5);
        dashboard.setDieOnCell(0,3,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[0][3].getDie().setNumber(4);
        dashboard.setDieOnCell(3,1,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[3][1].getDie().setNumber(2);
        dashboard.setDieOnCell(3,2,new Die(Color.VIOLET));
        dashboard.getMatrixScheme()[3][2].getDie().setNumber(5);

        Assertions.assertEquals(6, new Shades(1,2).scoreEffect(dashboard));
        Assertions.assertEquals(6, new Shades(3,4).scoreEffect(dashboard));
        Assertions.assertEquals(8, new Shades(5,6).scoreEffect(dashboard));


    }
}