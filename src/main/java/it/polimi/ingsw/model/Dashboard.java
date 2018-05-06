package it.polimi.ingsw.model;

import it.polimi.ingsw.model.restriction.Restriction;
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;
    private Player owner;
    private int favourToken;
    private Cell[][] matrixScheme;

    /*ublic Dashboard(Player owner, String schemeName) {
        this.matrixScheme = new Cell[ROW][COLUMN];
        this.owner = owner;
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("C:\\Users\\Marco\\Dropbox\\File JSON\\" + schemeName));

            this.favourToken = (Integer) jsonObject.get("favourToken");

            JSONArray first = (JSONArray) jsonObject.get("Restriction");
            for (int i = 0; i < ROW; i++)
                for (int j = 0; j < COLUMN; j++)
                    this.matrixScheme[i][j] = new Cell((Restriction) ((JSONArray) jsonObject.get(i)).get(j));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    public Dashboard(Cell[][] matrixScheme){

        this.matrixScheme=matrixScheme;



    } // da togliere quando è completo json

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

    public void setDieOnCell(int row, int column, Die myDie) {

            this.matrixScheme[row][column].setDie(myDie);
    }

}

