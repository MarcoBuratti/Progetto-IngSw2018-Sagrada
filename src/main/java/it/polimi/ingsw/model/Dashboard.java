package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotValidParametersException;
import it.polimi.ingsw.model.exception.OccupiedCellException;
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
*/

public class Dashboard {
    private static final int ROW = 4;
    private static final int COLUMN = 5;
    private Player owner;
    private int favourToken;
    private Cell[][] matrixScheme;

    /*public Dashboard(Player owner, String schemeName) {
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

    } // da togliere quando è completo json, utile solo per eseguire i test

    public Dashboard(Cell[][] matrixScheme, int favourToken){
        this.matrixScheme = matrixScheme;
        this.favourToken = favourToken;
    } // da togliere

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
        return favourToken;
    }

    /**
     * returns a copy of the matrix, before being passed is cloned
     * has non @param input
     * @return matrixScheme
     */
    public Cell[][] getMatrixScheme() throws OccupiedCellException {

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
            if ((row >= 0 && row < 4) && (column >= 0 && column < 5)) {
                try {
                    this.matrixScheme[row][column].setDie(myDie);
                }catch (Exception e) {
                    throw new OccupiedCellException();
                }
            } else {
                throw new NotValidParametersException();
            }
    }

    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                ret += this.matrixScheme[i][j].toString();
            }
            ret += "\n";
        }
        return ret;
    }

}
