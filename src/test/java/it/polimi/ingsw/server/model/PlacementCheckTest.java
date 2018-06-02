package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlacementCheckTest {

    @Test
    void isEmpty() throws NotValidParametersException, OccupiedCellException, NotValidValueException {


        Dashboard dashboard = new Dashboard("Scheme_Test");

        Assertions.assertTrue(new PlacementCheck().isEmpty(dashboard.getMatrixScheme()));

        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        Assertions.assertFalse(new PlacementCheck().isEmpty(dashboard.getMatrixScheme()));
    }

    @Test
    void checkDiceColor() {

        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.GREEN);
        Die die3 = new Die(Color.RED);
        Die die4 = new Die(Color.YELLOW);

        Assertions.assertTrue(new PlacementCheck().checkDiceColor(die1, die2));
        Assertions.assertTrue(new PlacementCheck().checkDiceColor(die1, die1));
        Assertions.assertFalse(new PlacementCheck().checkDiceColor(die1, die3));
        Assertions.assertFalse(new PlacementCheck().checkDiceColor(die2, die4));
    }

    @Test
    void checkDiceNumber() throws NotValidValueException {
        Die die1 = new Die(Color.GREEN);
        die1.setNumber(5);
        Die die2 = new Die(Color.GREEN);
        die2.setNumber(5);
        Assertions.assertTrue( new PlacementCheck().checkDiceNumber(die1, die2));
        Assertions.assertTrue( new PlacementCheck().checkDiceNumber(die1, die1));
        Die die3 = new Die(Color.GREEN);
        die3.setNumber(6);
        Assertions.assertFalse( new PlacementCheck().checkDiceNumber(die1, die3) );
    }

    @Test
    void allowedNeighbours() throws NotValidValueException, NotValidParametersException, OccupiedCellException {


        Dashboard dashboard = new Dashboard("Scheme_Test");

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

        Assertions.assertFalse(new PlacementCheck().allowedNeighbours(1,1, die5, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new PlacementCheck().allowedNeighbours(0,1, die4, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new PlacementCheck().allowedNeighbours(3,0, die5, dashboard.getMatrixScheme()));

        die4.setNumber(1);
        Assertions.assertFalse(new PlacementCheck().allowedNeighbours(0,1, die4, dashboard.getMatrixScheme()));

    }

    @Test
    void nearBy() throws NotValidValueException, NotValidParametersException, OccupiedCellException {

        Dashboard dashboard = new Dashboard("Scheme_Test");
        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.RED);
        Die die3 = new Die(Color.YELLOW);
        die1.setNumber(1);
        die2.setNumber(2);
        die3.setNumber(3);
        dashboard.setDieOnCell(0, 0, die1);
        dashboard.setDieOnCell(0, 3, die2);
        dashboard.setDieOnCell(1, 2, die3);

        Assertions.assertTrue(new PlacementCheck().nearBy(2,1, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new PlacementCheck().nearBy(2,2, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().nearBy(3,3, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().nearBy(3,4, dashboard.getMatrixScheme()));

        Die die4 = new Die(Color.RED);
        die4.setNumber(4);
        Die die5 = new Die(Color.BLUE);
        die5.setNumber(5);
        Die die6 = new Die(Color.GREEN);
        die6.setNumber(6);
        Die die7 = new Die(Color.VIOLET);
        die7.setNumber(6);
        dashboard.setDieOnCell(0, 2, die4);
        dashboard.setDieOnCell(0, 4, die5);
        dashboard.setDieOnCell(1, 1, die6);
        dashboard.setDieOnCell(3, 2, die7);


        Assertions.assertTrue(new PlacementCheck().nearBy(2,1, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new PlacementCheck().nearBy(3,1, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().nearBy(3,0, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().nearBy(2,4, dashboard.getMatrixScheme()));
    }

    @Test
    void genericCheck() throws NotValidParametersException, NotValidValueException, OccupiedCellException {


        Dashboard dashboard = new Dashboard("Scheme_Test");
        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.BLUE);
        Die die3 = new Die(Color.GREEN);

        die1.setNumber(5);
        die2.setNumber(5);
        die3.setNumber(3);


        Assertions.assertTrue(new PlacementCheck().genericCheck(0, 3, die1, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().genericCheck(2,2, die2, dashboard.getMatrixScheme()));
        dashboard.setDieOnCell(0, 3, die1);
        Assertions.assertFalse(new PlacementCheck().genericCheck(0, 2, die2, dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().genericCheck(1,3, die3, dashboard.getMatrixScheme()));
        Assertions.assertTrue(new PlacementCheck().genericCheck(1,4, die2, dashboard.getMatrixScheme()));
        Die die4 =new Die(Color.RED);
        die4.setNumber(1);
        Assertions.assertTrue(new PlacementCheck().genericCheck(1,3,die4,dashboard.getMatrixScheme()));
        Assertions.assertFalse(new PlacementCheck().genericCheck(1, 1, die2, dashboard.getMatrixScheme()));

    }
}