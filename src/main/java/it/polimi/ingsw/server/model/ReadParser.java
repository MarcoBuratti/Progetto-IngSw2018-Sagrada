package it.polimi.ingsw.server.model;

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

class ReadParser {
    private static final int ROW = 4;
    private static final int COLUMN = 5;

    private static Cell[][] matrixScheme;
    private static long favourToken;
    private static final JSONParser parser = new JSONParser();

    private ReadParser () {
        throw new IllegalStateException();
    }

    static synchronized void createMatrixFromScheme(Dashboard dashboard, String schemeName) {
        try {
            matrixScheme = new Cell[ROW][COLUMN];
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/dashboard/" + schemeName + ".json"));

            favourToken = (Long) jsonObject.get("favourToken");

            AbstractRestriction restrictionFactory = new RestrictionFactory();
            JSONArray first = (JSONArray) jsonObject.get("Restriction");

            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {
                    matrixScheme[i][j] = new Cell(restrictionFactory.getRestriction(RestrictionEnum.valueOf((String) ((JSONArray) first.get(i)).get(j))));
                }
            }

            dashboard.setMatrixScheme( getMatrixSchemeCopy() );
            dashboard.setFavourToken( favourToken );
        } catch (IOException | ParseException e) {
            System.out.println(e.toString());
        } catch (NotValidValueException e) {
            e.printStackTrace();
        }
    }

    private static Cell[][] getMatrixSchemeCopy () {
        Cell[][] matrixSchemeCopy = new Cell[ROW][COLUMN];
        try {
            for (int i = 0; i < ROW; i++)
                for (int j = 0; j < COLUMN; j++)
                    matrixSchemeCopy[i][j] = matrixScheme[i][j].copyConstructor();
        } catch (OccupiedCellException e) {
            e.printStackTrace();
        }

        return matrixSchemeCopy;
    }


    static long getFavourToken() {
        return favourToken;
    }


}