package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckPositioningTest {

    @Test
    void isEmpty() throws NotValidParametersException, OccupiedCellException, NotValidNumberException {


        Dashboard dashboard = new Dashboard("Scheme Test");

        Assertions.assertTrue(new CheckPositioning().isEmpty(dashboard.getMatrixScheme()));

        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        Assertions.assertFalse(new CheckPositioning().isEmpty(dashboard.getMatrixScheme()));
    }

    @Test
    void checkDiceColor() {

        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.GREEN);
        Die die3 = new Die(Color.RED);
        Die die4 = new Die(Color.YELLOW);

        Assertions.assertTrue(new CheckPositioning().checkDiceColor(die1, die2));
        Assertions.assertTrue(new CheckPositioning().checkDiceColor(die1, die1));
        Assertions.assertFalse(new CheckPositioning().checkDiceColor(die1, die3));
        Assertions.assertFalse(new CheckPositioning().checkDiceColor(die2, die4));
    }

    @Test
    void checkDiceNumber() throws NotValidNumberException {
        Die die1 = new Die(Color.GREEN);
        die1.setNumber(5);
        Die die2 = new Die(Color.GREEN);
        die2.setNumber(5);
        Assertions.assertTrue( new CheckPositioning().checkDiceNumber(die1, die2));
        Assertions.assertTrue( new CheckPositioning().checkDiceNumber(die1, die1));
        Die die3 = new Die(Color.GREEN);
        die3.setNumber(6);
        Assertions.assertFalse( new CheckPositioning().checkDiceNumber(die1, die3) );
    }

    @Test
    void allowedNeighbours() throws NotValidNumberException, NotValidParametersException, OccupiedCellException {


        Dashboard dashboard = new Dashboard("Scheme Test");

        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.RED);
        Die die3 = new Die(Color.YELLOW);
        Die die4 = new Die(Color.BLUE);
        Die die5 = new Die(Color.YELLOW);
        die1.setNumber(1);
        die2.setNumber(2);
        die3.setNumber(3);
        die4.setNumber(4);
        die5.setNumber(5);
        dashboard.setDieOnCell(0, 0, die1);
        dashboard.setDieOnCell(0, 3, die2);
        dashboard.setDieOnCell(1, 2, die3);

        Assertions.assertFalse(new CheckPositioning().allowedNeighbours(1,1, die5, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new CheckPositioning().allowedNeighbours(0,1, die4, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new CheckPositioning().allowedNeighbours(3,0, die5, dashboard.getMatrixScheme()));

        die4.setNumber(1);
        Assertions.assertFalse(new CheckPositioning().allowedNeighbours(0,1, die4, dashboard.getMatrixScheme()));

    }

    @Test
    void nearBy() throws NotValidNumberException, NotValidParametersException, OccupiedCellException {

        Dashboard dashboard = new Dashboard("Scheme Test");
        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.RED);
        Die die3 = new Die(Color.YELLOW);;
        die1.setNumber(1);
        die2.setNumber(2);
        die3.setNumber(3);
        dashboard.setDieOnCell(0, 0, die1);
        dashboard.setDieOnCell(0, 3, die2);
        dashboard.setDieOnCell(1, 2, die3);

        Assertions.assertTrue(new CheckPositioning().nearBy(2,1, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new CheckPositioning().nearBy(2,2, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new CheckPositioning().nearBy(3,3, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new CheckPositioning().nearBy(3,4, dashboard.getMatrixScheme()));
    }

    @Test
    void genericCheck() throws NotValidParametersException, NotValidNumberException, OccupiedCellException {


        Dashboard dashboard = new Dashboard("Scheme Test");
        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.BLUE);
        Die die3 = new Die(Color.GREEN);

        die1.setNumber(5);
        die2.setNumber(5);
        die3.setNumber(3);


        Assertions.assertTrue(new CheckPositioning().genericCheck(0, 3, die1, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new CheckPositioning().genericCheck(2,2, die2, dashboard.getMatrixScheme()));
        dashboard.setDieOnCell(0, 3, die1);
        Assertions.assertFalse(new CheckPositioning().genericCheck(0, 2, die2, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new CheckPositioning().genericCheck(1,3, die3, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new CheckPositioning().genericCheck(1,4, die2, dashboard.getMatrixScheme()));
        Die die4 =new Die(Color.RED);
        die4.setNumber(1);
        Assertions.assertTrue(new CheckPositioning().genericCheck(1,3,die4,dashboard.getMatrixScheme()));
        Assertions.assertFalse(new CheckPositioning().genericCheck(1, 1, die2, dashboard.getMatrixScheme()));

    }
}