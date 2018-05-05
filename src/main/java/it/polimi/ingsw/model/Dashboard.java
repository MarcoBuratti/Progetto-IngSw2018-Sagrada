package ProvaMain.Prova;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;
    private Player owner;
    private int favourToken;
    private Cell[][] matrixScheme;

    public Dashboard(Player owner, String schemeName) {
        this.matrixScheme = new Cell[ROW][COLUMN];
        this.owner = owner;
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("C:\\Users\\Marco\\Dropbox\\File JSON\\" + schemeName));

            this.favourToken = (Integer) jsonObject.get("favourToken");

            JSONArray first = (JSONArray) jsonObject.get("Restriction");
            for (int i = 0; i < COLUMN_LENGTH; i++)
                for (int j = 0; j < ROW_LENGTH; j++)
                    this.matrixScheme[i][j] = new Cell((Restriction) ((JSONArray) jsonObject.get(i)).get(j));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Player getOwner() {
        return owner;
    }

    public int getFavourToken() {
        return favourToken;
    }

    public Cell[][] getMatrixScheme() {

        Cell[][] matrixScheme = new Cell[ROW][COLUMN];

        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COLUMN; j++)
                matrixScheme[i][j] = this.matrixScheme[i][j].clone();

        return matrixScheme;
    }

    public void setFavourToken(int favourToken) {
        this.favourToken = favourToken;
    }

    public boolean setDieOnCell(int row, int column, Die myDie) {

        if (genericCheck(row, column, myDie)) {
            this.matrixScheme[row][column].setDie(myDie);
            return true;
        }
        return false;
    }

    private boolean allowedNeighbours(int row, int column, Die myDie) {

        if (row > 0 && this.matrixScheme[row - 1][column].getUsedCell()) {
            if (checkDice(myDie, this.matrixScheme[row - 1][column].getDie()))
                return false;
        }
        if (row < 3 && this.matrixScheme[row + 1][column].getUsedCell()) {
            if (checkDice(myDie, this.matrixScheme[row + 1][column].getDie()))
                return false;
        }
        if (column > 0 && this.matrixScheme[row][column - 1].getUsedCell()) {
            if (checkDice(myDie, this.matrixScheme[row][column - 1].getDie()))
                return false;
        }
        if (column < 4 && this.matrixScheme[row][column + 1].getUsedCell()) {
            if (checkDice(myDie, this.matrixScheme[row][column + 1].getDie()))
                return false;
        }
        return true;
    }

    private boolean checkDice(Die die1, Die die2) {
        return (die1.getColour() == die2.getColour()) || (die1.getNumber() == die2.getNumber());
    }

    private boolean isEmpty() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (this.matrixScheme[i][j].getUsedCell()) return false;
            }
        }
        return true;
    }

    private boolean nearBy(int row, int column) {
        if (row > 0) {
            if (this.matrixScheme[row - 1][column].getUsedCell()) return true;
            if (column > 0)
                if (this.matrixScheme[row - 1][column - 1].getUsedCell() || this.matrixScheme[row][column - 1].getUsedCell())
                    return true;
            if (column < 4)
                if (this.matrixScheme[row - 1][column + 1].getUsedCell() || this.matrixScheme[row][column + 1].getUsedCell())
                    return true;
        }
        if (row < 3) {
            if (this.matrixScheme[row + 1][column].getUsedCell()) return true;
            if (column > 0)
                if (this.matrixScheme[row + 1][column - 1].getUsedCell() || this.matrixScheme[row][column - 1].getUsedCell())
                    return true;
            if (column < 4)
                if (this.matrixScheme[row + 1][column + 1].getUsedCell() || this.matrixScheme[row][column + 1].getUsedCell())
                    return true;
        }
        return false;
    }

    private boolean genericCheck(int row, int column, Die myDie) {

        if (!this.matrixScheme[row][column].allowedMove(myDie))
            return false;

        if (isEmpty()) {
            if (!(row == 0 || row == 4 || column == 0 || column == 5))
                return false;

        } else {
            if (!nearBy(row, column))
                return false;
        }

        if (!allowedNeighbours(row, column, myDie))
            return false;

        return true;
    }
}

