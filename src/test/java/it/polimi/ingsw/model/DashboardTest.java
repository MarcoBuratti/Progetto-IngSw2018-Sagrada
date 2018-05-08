
package it.polimi.ingsw.model;

import it.polimi.ingsw.model.restriction.NoRestriction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
 /**
class DashboardTest {

    @Test
    void getMatrixScheme() {
        Cell[][] matrixScheme = new Cell[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrixScheme[i][j] = new Cell(new NoRestriction());
            }
        }
        Dashboard dashboard = new Dashboard(matrixScheme);

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

        Cell[][] matrixScheme2 = new Cell[4][5];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                matrixScheme2[i][j] = matrixScheme[i][j].clone();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++) {
                System.out.println(matrixScheme2[i][j].getDie().getColour());
                System.out.println(matrixScheme2[i][j].getDie().getNumber());
            }
    }

    @Test
    void setDieOnCell() {
    }
}
  **/

