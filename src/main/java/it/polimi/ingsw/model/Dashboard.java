package it.polimi.ingsw.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Dashboard {
    private static final int ROW_LENGTH= 5;
    private static final int COLUMN_LENGTH = 4;
    private Player owner;
    private int favourToken;
    private Cell[][] matrixScheme;

    public Dashboard (Player owner, String schemeName) {
        this.matrixScheme = new Cell[ROW_LENGTH][COLUMN_LENGTH];
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
        return matrixScheme;
    }

    public void setFavourToken(int favourToken) {
        this.favourToken = favourToken;
    }

    public void setDieOnCell (int row, int column, Die myDie){
        if( matrixScheme[row-1][column-1].getUsedCell() == false )
            if (true) //Implementare i booleani per le regole
                matrixScheme[row-1][column-1].setDie(myDie);
    }
}
