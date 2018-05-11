package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidValueException;
import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
import it.polimi.ingsw.model.restriction.AbstractRestriction;
import it.polimi.ingsw.model.restriction.RestrictionEnum;
import it.polimi.ingsw.model.restriction.RestrictionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;
    private Player owner;
    private long favourToken;
    private Cell[][] matrixScheme;

    public Dashboard( String schemeName) throws NotValidValueException {
        this.matrixScheme = new Cell[ROW][COLUMN];
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("files/dashboard/"+schemeName+".json"));

            this.favourToken = (Long) jsonObject.get("favourToken");

            AbstractRestriction restrictionFactory = new RestrictionFactory();
            JSONArray first = (JSONArray) jsonObject.get("Restriction");

            for (int i = 0; i < ROW; i++)
                for (int j = 0; j < COLUMN; j++) {
                    this.matrixScheme[i][j] = new Cell(restrictionFactory.getRestriction (RestrictionEnum.valueOf((String)((JSONArray) first.get(i)).get(j))));
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns the name of the board owner
     * @return
     */
    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * return the number of favourToken
     * @return
     */
    public int getFavourToken() {
        return (int)favourToken;
    }

    /**
     * returns a copy of the matrix, before being passed is cloned
     * has non @param input
     * @return matrixScheme
     */
    public Cell[][] getMatrixScheme() {

        Cell[][] matrixScheme = new Cell[ROW][COLUMN];
        try {
            for (int i = 0; i < ROW; i++)
                for (int j = 0; j < COLUMN; j++)
                    matrixScheme[i][j] = this.matrixScheme[i][j].copyConstructor();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return matrixScheme;
    }

    /**
     * sets the die on cell (row, column) and does not perform any kind of restriction control
     * @param row
     * @param column
     * @param myDie
     */
    public void setDieOnCell(int row, int column, Die myDie) throws NotValidParametersException, OccupiedCellException {
            if ((row >= 0 && row < ROW) && (column >= 0 && column < COLUMN)) {
                try {
                    this.matrixScheme[row][column].setDie(myDie);
                }catch (Exception e) {
                    throw new OccupiedCellException();
                }
            } else {
                throw new NotValidParametersException();
            }
    }

    public boolean equalsScheme (Object myObject){
        if(myObject != null) {
            if (myObject.getClass() == this.getClass()) {
                Dashboard dashboard = (Dashboard) myObject;
                for (int row = 0; row < ROW; row++)
                    for (int col = 0; col < COLUMN; col++)
                        if (!this.matrixScheme[row][col].equals(dashboard.matrixScheme[row][col]))
                            return false;
                return true;
            } else return false;
        }else return false;
    }

    @Override
    public String toString() {
        String string = "Dashboard\n\n";
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                bld.append(this.matrixScheme[i][j].toString());
                bld.append(" ");
            }
            bld.append("\n");
        }
        string += bld.toString();
        return string;
    }

}
