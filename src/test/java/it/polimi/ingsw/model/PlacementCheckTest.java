package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.NotValidNumberException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlacementCheckTest {

    @Test
    void isEmpty() throws NotValidParametersException, OccupiedCellException {

        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }
        Dashboard dashboard = new Dashboard(matrixScheme);

        Assertions.assertTrue(new PlacementCheck().isEmpty(matrixScheme));

        dashboard.setDieOnCell(0,0,new Die(Color.GREEN));
        Assertions.assertFalse(new PlacementCheck().isEmpty(matrixScheme));
    }

    @Test
    void checkDiceColour() {

        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.GREEN);
        Die die3 = new Die(Color.RED);
        Die die4 = new Die(Color.YELLOW);

        Assertions.assertTrue(new PlacementCheck().checkDiceColour(die1, die2));
        Assertions.assertTrue(new PlacementCheck().checkDiceColour(die1, die1));
        Assertions.assertFalse(new PlacementCheck().checkDiceColour(die1, die3));
        Assertions.assertFalse(new PlacementCheck().checkDiceColour(die2, die4));
    }

    @Test
    void checkDiceNumber() throws NotValidNumberException {
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
    void allowedNeighbours() throws NotValidNumberException, NotValidParametersException, OccupiedCellException {

        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }

        Dashboard dashboard = new Dashboard(matrixScheme);
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

        Assertions.assertFalse(new PlacementCheck().allowedNeighbours(1,1, die5, matrixScheme));
        Assertions.assertTrue(new PlacementCheck().allowedNeighbours(0,1, die4, matrixScheme));
        Assertions.assertTrue(new PlacementCheck().allowedNeighbours(3,0, die5, matrixScheme));

        die4.setNumber(1);
        Assertions.assertFalse(new PlacementCheck().allowedNeighbours(0,1, die4, matrixScheme));

    }

    @Test
    void nearBy() throws NotValidNumberException, NotValidParametersException, OccupiedCellException {
        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }

        Dashboard dashboard = new Dashboard(matrixScheme);
        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.RED);
        Die die3 = new Die(Color.YELLOW);;
        die1.setNumber(1);
        die2.setNumber(2);
        die3.setNumber(3);
        dashboard.setDieOnCell(0, 0, die1);
        dashboard.setDieOnCell(0, 3, die2);
        dashboard.setDieOnCell(1, 2, die3);

        Assertions.assertTrue(new PlacementCheck().nearBy(2,1, matrixScheme));
        Assertions.assertTrue(new PlacementCheck().nearBy(2,2, matrixScheme));
        Assertions.assertFalse(new PlacementCheck().nearBy(3,3, matrixScheme));
        Assertions.assertFalse(new PlacementCheck().nearBy(3,4, matrixScheme));
    }

    @Test
    void genericCheck() throws NotValidParametersException, NotValidNumberException, OccupiedCellException {

        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }
        Dashboard dashboard = new Dashboard(matrixScheme);
        Die die1 = new Die(Color.GREEN);
        Die die2 = new Die(Color.BLUE);
        Die die3 = new Die(Color.GREEN);

        die1.setNumber(5);
        die2.setNumber(5);
        die3.setNumber(3);


        Assertions.assertTrue(new PlacementCheck().genericCheck(0, 3, die1, matrixScheme));
        Assertions.assertFalse(new PlacementCheck().genericCheck(2,2, die2, matrixScheme));
        dashboard.setDieOnCell(0, 3, die1);
        Assertions.assertFalse(new PlacementCheck().genericCheck(0, 2, die2, matrixScheme));
        Assertions.assertFalse(new PlacementCheck().genericCheck(1,3, die3, matrixScheme));
        Assertions.assertTrue(new PlacementCheck().genericCheck(1,4, die2, matrixScheme));
        Die die4 =new Die(Color.RED);
        die4.setNumber(1);
        Assertions.assertTrue(new PlacementCheck().genericCheck(1,3,die4,matrixScheme));
        Assertions.assertFalse(new PlacementCheck().genericCheck(1, 1, die2, matrixScheme));

    }
}