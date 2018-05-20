package it.polimi.ingsw.controller.action;

import it.polimi.ingsw.model.Die;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class PlayerMove {
    private int[] intMatrixParameters;
    private Optional<Boolean> twoReplace;
    private Optional<Boolean> needPlacement;

    private Optional<Integer> indexDie;
    private Optional<Die> die2;
    private Optional<Integer> setOnDie;

    private String typeMove;
    private Optional<String> toolName;

    public static PlayerMove PlayerMoveConstructor(){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/LastPlayerMove.json"));

            String moveType = (String) jsonObject.get("type_playerMove");

            switch (moveType){
                case "PlaceDie":
                    int die, row, column;
                    die = Integer.parseInt((String) jsonObject.get("Die"));
                    row = Integer.parseInt((String) jsonObject.get("Row"));
                    column = Integer.parseInt((String) jsonObject.get("Column"));
                    int[] coordinates = new int[]{row, column};
                    return new PlayerMove("PlaceDie", die, coordinates);

                case "UseTool":
                    String toolName = (String) jsonObject.get("Tool");
                    try {
                        switch (toolName) {
                            default:
                                throw new IllegalAccessException();

                        }
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                case "GoThrough":
                    return new PlayerMove("GoThrough");

                default: throw new IllegalArgumentException();

            }

        } catch (IOException | ParseException e ) {
            System.out.println(e.toString());
        }
        throw new IllegalArgumentException();
    }

    public PlayerMove(String typeMove){
        if(typeMove.equals("GoThrough"))
            this.typeMove=typeMove;
        else
            throw new IllegalArgumentException();
    }

    public PlayerMove(String typeMove, int indexDie, int[] intParameters){
        if(typeMove.equals("PlaceDie")){
            this.typeMove=typeMove;
            this.intMatrixParameters=intParameters.clone();
            this.indexDie=Optional.of(indexDie);
        }
    }

    public PlayerMove(String typeMove,int[] intParameters) {
        if (typeMove.equals("UseTool")) {
            this.typeMove = typeMove;
            this.intMatrixParameters = intParameters.clone();
            if (intParameters.length > 4) {
                this.twoReplace = Optional.of(true);
            } else
                this.twoReplace = Optional.of(false);
        }
    }

    public PlayerMove(String typeMove,int indexDie,int newValue) {
        if (typeMove.equals("usetool")) {
            this.typeMove = typeMove;
            this.indexDie=Optional.of(indexDie);
            this.setOnDie=Optional.of(newValue);
        }
    }

    public Integer getIntParameters(int index) {
        if(intMatrixParameters.length>0 && index < intMatrixParameters.length)
                return intMatrixParameters[index];
        else
           throw new IllegalArgumentException();
    }

    public Optional<Boolean> getNeedPlacement() {
        return needPlacement;
    }

    public Boolean getTwoReplace() {
        if(twoReplace.isPresent())
            return twoReplace.get();
        else
            throw new IllegalArgumentException();
    }

    public Integer getIndexDie() {
        if(indexDie.isPresent())
            return indexDie.get();
        else
            throw new IllegalArgumentException();
    }

    public Optional<Die> getDie2() {
        return die2;
    }

    public Integer getSetOnDie() {
        if(setOnDie.isPresent())
            return setOnDie.get();
        else
            throw new IllegalArgumentException();
    }

    public String getTypeMove() {
        return typeMove;
    }
}


