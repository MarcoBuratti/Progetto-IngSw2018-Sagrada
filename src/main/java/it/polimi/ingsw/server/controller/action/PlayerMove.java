package it.polimi.ingsw.server.controller.action;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class PlayerMove implements Serializable {
    private static final String PLACE_DIE = "PlaceDie";
    private static final String USE_TOOL = "UseTool";
    private static final String GO_THROUGH = "GoThrough";
    private String playerNickname;
    private String moveType;
    private ArrayList<Integer> intParameters;
    private Boolean twoReplace;
    private Boolean addOne;
    private Integer indexDie;
    private Integer extractedToolIndex;

    //TODO PlayerMove(playerNickname, moveType, dieIndex, coordinates)

    public static PlayerMove playerMoveConstructor() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/LastPlayerMove.json"));
            return playerMoveReader(jsonObject);
        } catch (IOException | ParseException e) {
            System.err.println(e.toString());
        }
        throw new IllegalArgumentException();
    }

    public static PlayerMove playerMoveReader ( JSONObject jsonObject ) {

        String playerNickname = (String) jsonObject.get("playerID");
        String moveType = (String) jsonObject.get("type_playerMove");
        int dieIndex;
        ArrayList<Integer> coordinates;
        int otherKeysSize;



        switch (moveType) {
            case PLACE_DIE:
                dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                otherKeysSize = jsonObject.size() - 3;
                coordinates = new ArrayList<>();
                for ( int i = 0; i < otherKeysSize; i++ )
                    coordinates.add(Integer.parseInt((String) jsonObject.get("Key" + (i + 2))));
                return new PlayerMove(playerNickname, moveType, dieIndex, coordinates);

            case USE_TOOL:
                int toolIndex = Integer.parseInt((String) jsonObject.get("toolIndex"));
                int extractedToolIndex = Integer.parseInt((String) jsonObject.get("extractedToolIndex"));
                System.out.println(playerNickname + " " + moveType + " " + toolIndex + " SWITCH");
                switch (toolIndex) {

                    case 7:
                        System.out.println( toolIndex + " CASE 7");
                        dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                        String boolKey = ((String) jsonObject.get("Key2"));
                        boolean bool;
                        bool = ( boolKey.equals("1") );
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, dieIndex, bool);

                    case 0:
                    case 2:
                    case 8:
                    case 11:
                        otherKeysSize = jsonObject.size() - 4;
                        coordinates = new ArrayList<>();
                        for ( int i = 0 ; i < otherKeysSize ; i++ ) {
                            coordinates.add(Integer.parseInt((String) jsonObject.get("Key" + (i + 1))));
                            System.out.println(coordinates + " " + toolIndex + " CASE 0,2,8,11");
                        }
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, coordinates);

                    case 1:
                    case 9:
                    case 10:
                        dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                        otherKeysSize = jsonObject.size() - 5;
                        coordinates = new ArrayList<>();
                        System.out.println("DieIndex " + dieIndex);
                        for ( int i = 0 ; i < otherKeysSize ; i++ ) {
                            coordinates.add(Integer.parseInt((String) jsonObject.get("Key" + (i + 2))));
                            System.out.println(coordinates + " " + toolIndex + " CASE 1,9,10");
                        }
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, dieIndex, coordinates);

                    case 3:
                    case 6:
                    case 4:
                        dieIndex = Integer.parseInt((String) jsonObject.get("Key1"));
                        System.out.println(   toolIndex +  " CASE 3,4,6");
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex, dieIndex);

                    case 5:
                        System.out.println("CASE 5");
                        return new PlayerMove(playerNickname, moveType, extractedToolIndex);

                    default:
                        break;
                }
                throw new IllegalArgumentException();


            case GO_THROUGH:
                return new PlayerMove(playerNickname, moveType);

            default:
                throw new IllegalArgumentException();
        }
    }

    public PlayerMove(String playerNickname, String moveType) {
        this.playerNickname = playerNickname;
        if (moveType.equals(GO_THROUGH))
            this.moveType = moveType;
        else
            throw new IllegalArgumentException();
    }

    //tool 7(OK)
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex) {
        this.playerNickname = playerNickname;
        this.moveType = moveType;
        this.extractedToolIndex = extractedToolIndex;
    }

    //place die, usato anche da tool 11 OPPURE tool 2, 3, 4, 12
    public PlayerMove(String playerNickname, String moveType, int firstParameter, ArrayList<Integer> intParameters) {
        this.playerNickname = playerNickname;
        this.moveType = moveType;
        this.intParameters = intParameters;
        if ( moveType.equals(PLACE_DIE) ) {
            this.indexDie = firstParameter;
        }
        else if ( moveType.equals(USE_TOOL) ) {
            this.extractedToolIndex = firstParameter;
            this.twoReplace = intParameters.size() > 4;
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 5 ,9(OK) e 8(OK ma dubbio su come segnarlo)
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex, int indexDie, ArrayList<Integer> intParameters) {
        this.playerNickname = playerNickname;
        this.moveType = moveType;
        this.extractedToolIndex = extractedToolIndex;
        this.intParameters = intParameters;
        this.indexDie = indexDie;
    }

    //tool 1(OK)
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex, int indexDie, boolean addOne) {
        this.playerNickname = playerNickname;
        this.extractedToolIndex = extractedToolIndex;
        this.moveType = moveType;
        this.indexDie = indexDie;
        this.addOne = addOne;
    }

    //tool 6(OK), 10(OK) e 11(OK)
    public PlayerMove(String playerNickname, String moveType, int extractedToolIndex, int indexDie) {
        this.playerNickname = playerNickname;
        this.extractedToolIndex = extractedToolIndex;
        this.moveType = moveType;
        this.indexDie = indexDie;
    }

    public String getPlayerNickname() {
        return this.playerNickname;
    }

    public Integer getIntParameters(int index) {
        if (intParameters.size() > 0 && index < intParameters.size()) {
            Integer newInt = intParameters.get(index);
            return newInt;
        }
        else
            throw new IllegalArgumentException();
    }

    public Optional<Boolean> getTwoReplace() {
        return Optional.ofNullable(twoReplace);
    }

    public Optional<Integer> getIndexDie() {
        return Optional.ofNullable(indexDie);
    }

    public Optional<Boolean> getAddOne() {
        return Optional.ofNullable(addOne);
    }

    public Optional<Integer> getExtractedToolIndex() {
        return Optional.ofNullable(extractedToolIndex);
    }

    public String getMoveType() {
        return moveType;
    }

    public String toString() {
        switch (moveType) {
            case PLACE_DIE:
                return "Die:" + this.indexDie + " " + "coordinates:" + this.intParameters;
            case GO_THROUGH:
                return "Go Through";
            case USE_TOOL:
                return "Use Tool";
            default:
                return "";
        }

    }
}


