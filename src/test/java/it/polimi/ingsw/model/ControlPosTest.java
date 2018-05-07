package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NumberNotValidException;
import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlPosTest {

    @Test
    void isEmpty() {

        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }
        Dashboard dashboard = new Dashboard(matrixScheme);

        Assertions.assertTrue(new ControlPos().isEmpty(matrixScheme));

        dashboard.setDieOnCell(0,0,new Die(Colour.GREEN));
        Assertions.assertFalse(new ControlPos().isEmpty(matrixScheme));
    }

    @Test
    void checkDiceColour() {

        Die die1 = new Die(Colour.GREEN);
        Die die2 = new Die(Colour.GREEN);
        Die die3 = new Die(Colour.RED);
        Die die4 = new Die(Colour.YELLOW);

        Assertions.assertTrue(new ControlPos().checkDiceColour(die1, die2));
        Assertions.assertTrue(new ControlPos().checkDiceColour(die1, die1));
        Assertions.assertFalse(new ControlPos().checkDiceColour(die1, die3));
        Assertions.assertFalse(new ControlPos().checkDiceColour(die2, die4));
    }

    @Test
    void checkDiceNumber() throws NumberNotValidException {
        Die die1 = new Die(Colour.GREEN);
        die1.setNumber(5);
        Die die2 = new Die(Colour.GREEN);
        die2.setNumber(5);
        Assertions.assertTrue( new ControlPos().checkDiceNumber(die1, die2));
        Assertions.assertTrue( new ControlPos().checkDiceNumber(die1, die1));
        Die die3 = new Die(Colour.GREEN);
        die3.setNumber(6);
        Assertions.assertFalse( new ControlPos().checkDiceNumber(die1, die3) );
    }

    @Test
    void allowedNeighbours() throws NumberNotValidException {

        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }

        Dashboard dashboard = new Dashboard(matrixScheme);
        Die die1 = new Die(Colour.GREEN);
        Die die2 = new Die(Colour.RED);
        Die die3 = new Die(Colour.YELLOW);
        Die die4 = new Die(Colour.BLUE);
        Die die5 = new Die(Colour.YELLOW);
        die1.setNumber(1);
        die2.setNumber(2);
        die3.setNumber(3);
        die4.setNumber(4);
        die5.setNumber(5);
        dashboard.setDieOnCell(0, 0, die1);
        dashboard.setDieOnCell(0, 3, die2);
        dashboard.setDieOnCell(1, 2, die3);

        Assertions.assertFalse(new ControlPos().allowedNeighbours(1,1, die5, matrixScheme));
        Assertions.assertTrue(new ControlPos().allowedNeighbours(0,1, die4, matrixScheme));
        Assertions.assertTrue(new ControlPos().allowedNeighbours(3,0, die5, matrixScheme));

        die4.setNumber(1);
        Assertions.assertFalse(new ControlPos().allowedNeighbours(0,1, die4, matrixScheme));

    }

    @Test
    void nearBy() throws NumberNotValidException {
        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }

        Dashboard dashboard = new Dashboard(matrixScheme);
        Die die1 = new Die(Colour.GREEN);
        Die die2 = new Die(Colour.RED);
        Die die3 = new Die(Colour.YELLOW);;
        die1.setNumber(1);
        die2.setNumber(2);
        die3.setNumber(3);
        dashboard.setDieOnCell(0, 0, die1);
        dashboard.setDieOnCell(0, 3, die2);
        dashboard.setDieOnCell(1, 2, die3);

        Assertions.assertTrue(new ControlPos().nearBy(2,1, matrixScheme));
        Assertions.assertTrue(new ControlPos().nearBy(2,2, matrixScheme));
        Assertions.assertFalse(new ControlPos().nearBy(3,3, matrixScheme));
        Assertions.assertFalse(new ControlPos().nearBy(3,4, matrixScheme));
    }
}