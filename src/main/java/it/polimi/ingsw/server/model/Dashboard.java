package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exception.NotValidParametersException;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import it.polimi.ingsw.server.model.exception.OccupiedCellException;
import it.polimi.ingsw.server.model.restriction.AbstractRestriction;
import it.polimi.ingsw.server.model.restriction.RestrictionEnum;
import it.polimi.ingsw.server.model.restriction.RestrictionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;
    private long favourToken;
    private Cell[][] matrixScheme;

    public Dashboard ( String schemeName ) {
        this.matrixScheme = new Cell[ROW][COLUMN];
        ReadParser.createMatrixFromScheme(this, schemeName);
    }

    /**
     * return the number of favourToken
     *
     * @return
     */
    public int getFavourToken() {
        return (int) favourToken;
    }

    /**
     *
     *
     * @param favourToken
     */
    void setFavourToken (long favourToken) {
        this.favourToken = favourToken;
    }

    /**
     * returns a copy of the matrix, before being passed is cloned
     * has non @param input
     *
     * @return matrixScheme
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

    void setMatrixScheme (Cell[][] matrixScheme) {
        this.matrixScheme = matrixScheme;
    }

    /**
     * sets the die on cell (row, column) and does not perform any kind of restriction control
     *
     * @param row
     * @param column
     * @param myDie
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


    public Die removeDieFromCell(int row, int column) throws NotValidParametersException {
        if ((row >= 0 && row < ROW) && (column >= 0 && column < COLUMN) ) {
            Die die = this.matrixScheme[row][column].removeDie();
            if ( die != null )
                return die;
            else throw new NotValidParametersException();
        } else {
            throw new NotValidParametersException();
        }
    }


    boolean equalsScheme( Dashboard dashboard ) {
        for (int row = 0; row < ROW; row++)
            for (int col = 0; col < COLUMN; col++)
                if (!this.matrixScheme[row][col].cellEquals(dashboard.matrixScheme[row][col]))
                    return false;
        return true;
    }

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
