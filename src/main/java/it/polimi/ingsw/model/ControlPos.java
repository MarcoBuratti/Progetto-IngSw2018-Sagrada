package it.polimi.ingsw.model;


public class ControlPos {

    private static final int ROW = 4;
    private static final int COLUMN = 5;

    public boolean isEmpty(Cell[][] matrixScheme) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (matrixScheme[i][j].getUsedCell()) return false;
            }
        }
        return true;
    }

    public boolean checkDiceColour(Die die1, Die die2) {
        return (die1.getColour() == die2.getColour());
    }

    public boolean checkDiceNumber(Die die1, Die die2) {
        return (die1.getNumber() == die2.getNumber());
    }

    public boolean allowedNeighbours(int row, int column, Die myDie, Cell[][] matrixScheme) {

        if (row > 0 && matrixScheme[row - 1][column].getUsedCell()) {
            Die die = matrixScheme[row - 1][column].getDie();
            if (checkDiceColour(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (row < 3 && matrixScheme[row + 1][column].getUsedCell()) {
            Die die = matrixScheme[row + 1][column].getDie();
            if (checkDiceColour(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (column > 0 && matrixScheme[row][column - 1].getUsedCell()) {
            Die die = matrixScheme[row][column - 1].getDie();
            if (checkDiceColour(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        if (column < 4 && matrixScheme[row][column + 1].getUsedCell()) {
            Die die = matrixScheme[row][column + 1].getDie();
            if (checkDiceColour(myDie, die) || checkDiceNumber(myDie, die))
                return false;
        }
        return true;
    }

    public boolean genericCheck(int row, int column, Die myDie, Cell[][] matrixScheme) {

        if (!matrixScheme[row][column].allowedMove(myDie))
            return false;

        if (isEmpty(matrixScheme)) {
            if (row == 0 || row == 4 || column == 0 || column == 5) {
                return true;
            } else return false;

        } else {
            if (!nearBy(row, column, matrixScheme))
                return false;
        }

        if (!allowedNeighbours(row, column, myDie, matrixScheme))
            return false;

        return true;
    }

    public boolean nearBy(int row, int column, Cell[][] matrixScheme) {
        if (row > 0) {
            if (matrixScheme[row - 1][column].getUsedCell()) return true;
            if (column > 0)
                if (matrixScheme[row - 1][column - 1].getUsedCell() || matrixScheme[row][column - 1].getUsedCell())
                    return true;
            if (column < 4)
                if (matrixScheme[row - 1][column + 1].getUsedCell() || matrixScheme[row][column + 1].getUsedCell())
                    return true;
        }
        if (row < 3) {
            if (matrixScheme[row + 1][column].getUsedCell()) return true;
            if (column > 0)
                if (matrixScheme[row + 1][column - 1].getUsedCell() || matrixScheme[row][column - 1].getUsedCell())
                    return true;
            if (column < 4)
                if (matrixScheme[row + 1][column + 1].getUsedCell() || matrixScheme[row][column + 1].getUsedCell())
                    return true;
        }
        return false;
    }

}
