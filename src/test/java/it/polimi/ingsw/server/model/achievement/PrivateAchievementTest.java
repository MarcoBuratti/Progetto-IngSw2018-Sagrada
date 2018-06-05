package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Dashboard;
import it.polimi.ingsw.server.model.Die;
import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PrivateAchievementTest {

    @Test
    void scoreEffect() throws NotValidValueException, NotValidParametersException, OccupiedCellException {

    PrivateAchievement redAchievement =new PrivateAchievement(Color.RED);
    PrivateAchievement violetAchievement =new PrivateAchievement(Color.VIOLET);
    PrivateAchievement yellowAchievement =new PrivateAchievement(Color.YELLOW);
    PrivateAchievement greenAchievement =new PrivateAchievement(Color.GREEN);
    PrivateAchievement blueAchievement =new PrivateAchievement(Color.BLUE);

        Dashboard dashboard = new Dashboard("Scheme_Test");
        Assertions.assertEquals(0, redAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(0, greenAchievement.scoreEffect(dashboard));


        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        dashboard.getMatrixScheme()[0][0].getDie().setNumber(2);
        dashboard.setDieOnCell(1,0,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[1][0].getDie().setNumber(4);
        dashboard.setDieOnCell(2,0,new Die(Color.YELLOW));
        dashboard.getMatrixScheme()[2][0].getDie().setNumber(5);
        dashboard.setDieOnCell(3,3,new Die(Color.BLUE));
        dashboard.getMatrixScheme()[3][3].getDie().setNumber(2);

        Assertions.assertEquals(0, violetAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(6, blueAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(2, greenAchievement.scoreEffect(dashboard));

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

        Assertions.assertEquals(1, redAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(11, yellowAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(14, blueAchievement.scoreEffect(dashboard));

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

        Assertions.assertEquals(11, redAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(9, greenAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(21, blueAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(18, yellowAchievement.scoreEffect(dashboard));
        Assertions.assertEquals(16, violetAchievement.scoreEffect(dashboard));


    }
}