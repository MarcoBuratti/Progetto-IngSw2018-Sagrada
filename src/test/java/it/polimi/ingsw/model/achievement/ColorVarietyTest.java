package it.polimi.ingsw.model.achievement;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ColorVarietyTest {

    @Test
    void scoreEffect() throws NotValidParametersException, OccupiedCellException, NotValidNumberException {

        Dashboard dashboard = new Dashboard("Scheme Test");
        Assertions.assertEquals(0, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        dashboard.setDieOnCell(1,1,new Die(Color.BLUE));
        dashboard.setDieOnCell(2,2,new Die(Color.YELLOW));
        dashboard.setDieOnCell(3,3,new Die(Color.RED));

        Assertions.assertEquals(0, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,3,new Die(Color.VIOLET));

        Assertions.assertEquals(4, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(0,1,new Die(Color.RED));
        dashboard.setDieOnCell(0,2,new Die(Color.YELLOW));
        dashboard.setDieOnCell(3,4,new Die(Color.GREEN));
        dashboard.setDieOnCell(0,4,new Die(Color.BLUE));
        dashboard.setDieOnCell(2,4,new Die(Color.VIOLET));

        Assertions.assertEquals(8, new ColorVariety().scoreEffect(dashboard));

        dashboard.setDieOnCell(1,0,new Die(Color.RED));
        dashboard.setDieOnCell(1,2,new Die(Color.YELLOW));
        dashboard.setDieOnCell(1,3,new Die(Color.GREEN));
        dashboard.setDieOnCell(1,4,new Die(Color.BLUE));
        dashboard.setDieOnCell(2,0,new Die(Color.VIOLET));
        dashboard.setDieOnCell(2,1,new Die(Color.RED));
        dashboard.setDieOnCell(2,3,new Die(Color.YELLOW));
        dashboard.setDieOnCell(3,0,new Die(Color.GREEN));
        dashboard.setDieOnCell(3,1,new Die(Color.BLUE));
        dashboard.setDieOnCell(3,2,new Die(Color.VIOLET));

        Assertions.assertEquals(16, new ColorVariety().scoreEffect(dashboard));
    }

}