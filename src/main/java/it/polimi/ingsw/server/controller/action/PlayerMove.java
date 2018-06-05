package it.polimi.ingsw.server.controller.action;

import it.polimi.ingsw.server.controller.tool.ToolNames;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public class PlayerMove implements Serializable {
    private String playerNickname;
    private String typeMove;
    private int[] intMatrixParameters;
    private Boolean twoReplace;
    private Boolean addOne;
    private Integer indexDie;
    private ToolNames toolName;



    public static PlayerMove PlayerMoveConstructor() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/LastPlayerMove.json"));
            return PlayerMoveReader(jsonObject);
        } catch (IOException | ParseException e) {
            System.err.println(e.toString());
        }
        throw new IllegalArgumentException();
    }



        public static PlayerMove PlayerMoveReader(JSONObject jsonObject) {

        String playerNickname = (String) jsonObject.get("playerID");
        String moveType = (String) jsonObject.get("type_playerMove");


        switch (moveType) {
            case "PlaceDie":
                int die, row, column;
                die = Integer.parseInt((String) jsonObject.get("Key1"));
                row = Integer.parseInt((String) jsonObject.get("Key2"));
                column = Integer.parseInt((String) jsonObject.get("Key3"));
                int[] coordinates = new int[]{row, column};
                return new PlayerMove(playerNickname, moveType, die, coordinates);

           /* case "UseTool":
                String toolName = (String) jsonObject.get("tool");
                try {
                    switch (toolName) {
                        default:
                            throw new IllegalAccessException();

                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

           */ case "GoThrough":
                return new PlayerMove(playerNickname, moveType);

            default:
                throw new IllegalArgumentException();
        }
    }

    public PlayerMove(String playerNickname, String typeMove){
        this.playerNickname = playerNickname;
        if(typeMove.equals("GoThrough"))
            this.typeMove = typeMove;
        else
            throw new IllegalArgumentException();
    }

    //tool 7(OK)
    public PlayerMove(String playerNickname, String typeMove,ToolNames toolName){
        this.playerNickname = playerNickname;
        if(typeMove.equals("UseTool")&&(toolName.equals(ToolNames.GLAZING_HAMMER))) {
            this.typeMove = typeMove;
            this.toolName = toolName;
        }
        else
            throw new IllegalArgumentException();
    }

    //usato anche da 11 per set dado(OK)
    public PlayerMove(String playerNickname, String typeMove, int indexDie, int[] intParameters){
        this.playerNickname = playerNickname;
        if(typeMove.equals("PlaceDie")){
            this.typeMove=typeMove;
            this.intMatrixParameters=intParameters.clone();
            this.indexDie = indexDie;
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 5 ,9(OK) e 8(OK ma dubbio su come segnarlo)
    public PlayerMove(String playerNickname, String typeMove, ToolNames toolName, int indexDie, int[] intParameters){
        this.playerNickname = playerNickname;
        if(typeMove.equals("UseTool")&&((toolName.equals(ToolNames.LENS_CUTTER)||toolName.equals(ToolNames.CORK_BAKED_STRAIGHTEDGE)||
                toolName.equals(ToolNames.RUNNING_PLIERS)))){
            this.typeMove=typeMove;
            this.toolName = toolName;
            this.intMatrixParameters=intParameters.clone();
            this.indexDie = indexDie;
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 2 3 4 e 12
    public PlayerMove(String playerNickname, String typeMove,ToolNames toolName,int[] intParameters) {
        this.playerNickname = playerNickname;
        if (typeMove.equals("UseTool")&&(toolName.equals(ToolNames.EGLOMISE_BRUSH)||toolName.equals(ToolNames.COPPER_FOIL_BURNISHER)||
                toolName.equals(ToolNames.LATHEKIN)||toolName.equals(ToolNames.TAP_WHEEL))){
            this.typeMove = typeMove;
            this.toolName = toolName;
            this.intMatrixParameters = intParameters.clone();
            if (intParameters.length > 4) {
                this.twoReplace = true;
            } else
                this.twoReplace = false;
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 1(OK)
    public PlayerMove(String playerNickname, String typeMove,ToolNames toolName,int indexDie,boolean addOne) {
        this.playerNickname = playerNickname;
        if (typeMove.equals("UseTool")&&toolName.equals(ToolNames.GROZING_PLIERS)) {
            this.toolName= toolName;
            this.typeMove = typeMove;
            this.indexDie=indexDie;
            this.addOne = addOne;
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 6(OK), 10(OK) e 11(OK)
    public PlayerMove(String playerNickname, String typeMove,ToolNames toolName,int indexDie) {

        if ((typeMove.equals("UseTool"))&&(toolName.equals(ToolNames.GRINDING_STONE)||toolName.equals(ToolNames.FLUX_BRUSH)
                || toolName.equals(ToolNames.FLUX_REMOVER))){
            this.toolName= toolName;
            this.typeMove = typeMove;
            this.indexDie= indexDie;
        }
        else
            throw new IllegalArgumentException();
    }

    public String getPlayerNickname(){
        return this.playerNickname;
    }

    public Integer getIntParameters(int index) {
        if(intMatrixParameters.length>0 && index < intMatrixParameters.length)
                return intMatrixParameters[index];
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

    public Optional<ToolNames> getToolName() {
            return Optional.ofNullable(toolName); }


    public String getTypeMove() {
        return typeMove;
    }

    public String toString(){
        switch (typeMove){
            case "PlaceDie":
                return "Die:" + this.indexDie + " " + "coordinates:" + this.intMatrixParameters[0] + this.intMatrixParameters[1];
            case "GoThrough":
                return "Go Through";
            case "UseTool":
                return "Use Tool";
            default:
                return "";
        }

    }
}


