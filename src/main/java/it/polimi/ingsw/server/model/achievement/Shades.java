package it.polimi.ingsw.server.model.achievement;

import it.polimi.ingsw.server.model.Cell;
import it.polimi.ingsw.server.model.Dashboard;

public class Shades implements CardAchievement {

    private int number1;
    private int number2;

    public Shades(int number1, int number2) {
        this.number1 = number1;
        this.number2 = number2;
    }

    public int scoreEffect(Dashboard dashboard) {

        Cell[][] matrixScheme = dashboard.getMatrixScheme();
        int count1 = 0;
        int count2 = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++)
                if (matrixScheme[i][j].getUsedCell()) {
                    int numberDie = matrixScheme[i][j].getDie().getNumber();
                    if (numberDie == number1)
                        count1++;
                    else if (numberDie == number2)
                        count2++;
                }

        }
        return Math.min(count1, count2) * 2;

    }

    @Override
    public String toString() {
        String string;
        switch (this.number1) {
            case 1:
                string = "Light Shades: ";
                break;
            case 3:
                string = "Medium Shades: ";
                break;
            case 5:
                string = "Dark Shades: ";
                break;
            default:
                string = "Shades: ";
                break;
        }
        string += "Sets of " + this.number1 + " & " + this.number2 + " values anywhere.\n";
        return string;
    }
}

