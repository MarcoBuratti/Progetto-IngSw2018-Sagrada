package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;

public class Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;
    private long favourToken;
    private Cell[][] matrixScheme;

    /**
     * Creates a Dashboard Object, setting its matrixScheme as an empty matrix having all the restrictions imposed by the scheme having the selected name
     * and also setting the favourToken attribute as the number of tokens associated with the scheme.
     *
     * @param schemeName the name of the selected scheme
     */
    public Dashboard(String schemeName) {
        DashboardParser.createMatrixFromScheme(this, schemeName);
    }

    /**
     * Returns the number of favour tokens associated with the selected scheme.
     *
     * @return the favourToken attribute
     */
    public int getFavourToken() {
        return (int) favourToken;
    }

    /**
     * Allows the user to set the favourToken attribute as the number of favour tokens given to the player when choosing the scheme.
     *
     * @param favourToken the number of tokens given to the player at the start
     */
    void setFavourToken(long favourToken) {
        this.favourToken = favourToken;
    }

    /**
     * Returns a copy of the matrix of Cell Objects
     *
     * @return a copy of the matrixScheme attribute
     */
    public Cell[][] getMatrixScheme() {

        Cell[][] matrixSchemeCopy = new Cell[ROW][COLUMN];
        try {
            for (int i = 0; i < ROW; i++)
                for (int j = 0; j < COLUMN; j++)
                    matrixSchemeCopy[i][j] = this.matrixScheme[i][j].copyConstructor();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return matrixSchemeCopy;
    }

    /**
     * Allows the user to set the matrixScheme attribute.
     *
     * @param matrixScheme the matrix the user wants to set as matrixScheme attribute
     */
    void setMatrixScheme(Cell[][] matrixScheme) {
        this.matrixScheme = matrixScheme;
    }

    /**
     * Allows the user to place the selected die on the selected position of the matrix if the cell is empty, ignoring any kind of restriction.
     *
     * @param row    the row of the selected position of the matrix
     * @param column the column of the selected position of the matrix
     * @param myDie  the die the user wants to place
     */
    public void setDieOnCell(int row, int column, Die myDie) throws NotValidParametersException, OccupiedCellException {
        if ((row >= 0 && row < ROW) && (column >= 0 && column < COLUMN)) {
            try {
                this.matrixScheme[row][column].setDie(myDie);
            } catch (Exception e) {
                throw new OccupiedCellException();
            }
        } else {
            throw new NotValidParametersException();
        }
    }

    /**
     * Allows the user to remove a die from the matrix.
     *
     * @param row    the row of the selected position of the matrix
     * @param column the column of the selected position of the matrix
     * @return the Die Object removed from the specified cell
     * @throws NotValidParametersException if the cell is not occupied or if the specified coordinates are not valid
     */
    public Die removeDieFromCell(int row, int column) throws NotValidParametersException {
        if ((row >= 0 && row < ROW) && (column >= 0 && column < COLUMN)) {
            Die die = this.matrixScheme[row][column].removeDie();
            if (die != null)
                return die;
            else throw new NotValidParametersException();
        } else {
            throw new NotValidParametersException();
        }
    }


    /**
     * Returns an int representing the number of the empty cells on the matrix.
     *
     * @return a boolean
     */
    public int emptyCells() {
        int ret = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (!this.matrixScheme[i][j].getUsedCell())
                    ret++;

            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (this.matrixScheme[i][j].getUsedCell()) {
                    bld.append(this.matrixScheme[i][j].getRestriction().toString()).append("[").append(this.matrixScheme[i][j].getDie().toString()).append("]");
                    bld.append(" ");
                } else {
                    bld.append(this.matrixScheme[i][j].getRestriction().toString()).append("\033[0m").append("[  ]");
                    bld.append(" ");
                }
            }
            bld.append("!");
        }
        return bld.toString();
    }

}
