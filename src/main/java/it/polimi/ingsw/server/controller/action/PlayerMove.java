package it.polimi.ingsw.server.controller.action;

import it.polimi.ingsw.server.controller.tool.ToolNames;
import it.polimi.ingsw.server.model.Die;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PlayerMove implements Serializable {
    private int[] intMatrixParameters;
    private Optional<Boolean> twoReplace;
    private Optional<Boolean> addOne;

    private Optional<Integer> indexDie;
    private Optional<Die> die2;
    private Optional<Integer> setOnDie;

    private String typeMove;
    private Optional<ToolNames> toolName;



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

        String moveType = (String) jsonObject.get("type_playerMove");

        switch (moveType) {
            case "PlaceDie":
                int die, row, column;
                die = Integer.parseInt((String) jsonObject.get("Key1"));
                row = Integer.parseInt((String) jsonObject.get("Key2"));
                column = Integer.parseInt((String) jsonObject.get("Key3"));
                int[] coordinates = new int[]{row, column};
                return new PlayerMove("PlaceDie", die, coordinates);

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
                return new PlayerMove("GoThrough");

            default:
                throw new IllegalArgumentException();
        }
    }

    public PlayerMove(String typeMove){
        if(typeMove.equals("GoThrough"))
            this.typeMove=typeMove;
        else
            throw new IllegalArgumentException();
    }

    //tool 7 e 8
    public PlayerMove(String typeMove,ToolNames toolName){
        if((typeMove.equals("GoThrough"))||
                (typeMove.equals("UseTool")&&(toolName.equals(ToolNames.GLAZING_HAMMER)||toolName.equals(ToolNames.RUNNING_PLIERS)))) {
            this.typeMove = typeMove;
            this.toolName = Optional.of(toolName);
        }
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

    //tool 5 ,9 e 11
    public PlayerMove(String typeMove, ToolNames toolName, int indexDie, int[] intParameters){
        if(typeMove.equals("UseTool")&&((toolName.equals(ToolNames.LENS_CUTTER)||toolName.equals(ToolNames.CORK_BAKED_STRAIGHTEDGE)))){
            this.typeMove=typeMove;
            this.toolName=Optional.of(toolName);
            this.intMatrixParameters=intParameters.clone();
            this.indexDie=Optional.of(indexDie);
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 2 3 4 e 12
    public PlayerMove(String typeMove,ToolNames toolName,int[] intParameters) {
        if (typeMove.equals("UseTool")&&(toolName.equals(ToolNames.EGLOMISE_BRUSH)||toolName.equals(ToolNames.COPPER_FOIL_BURNISHER)||
                toolName.equals(ToolNames.LATHEKIN)||toolName.equals(ToolNames.TAP_WHEEL))){
            this.typeMove = typeMove;
            this.toolName=Optional.of(toolName);
            this.intMatrixParameters = intParameters.clone();
            if (intParameters.length > 4) {
                this.twoReplace = Optional.of(true);
            } else
                this.twoReplace = Optional.of(false);
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 1
    public PlayerMove(String typeMove,ToolNames toolName,int indexDie,boolean addOne) {
        if (typeMove.equals("UseTool")&&toolName.equals(ToolNames.GROZING_PLIERS)) {
            this.toolName=Optional.of(toolName);
            this.typeMove = typeMove;
            this.indexDie=Optional.of(indexDie);
            this.addOne=Optional.of(addOne);
        }
        else
            throw new IllegalArgumentException();
    }

    //tool 6 e 10
    public PlayerMove(String typeMove,ToolNames toolName,int indexDie) {
        if (typeMove.equals("UseTool")&&(toolName.equals(ToolNames.GRINDING_STONE)||toolName.equals(ToolNames.FLUX_BRUSH)||toolName.equals(ToolNames.FLUX_REMOVER))) {
            this.toolName=Optional.of(toolName);
            this.typeMove = typeMove;
            this.indexDie=Optional.of(indexDie);
        }
        else
            throw new IllegalArgumentException();
    }

    public Integer getIntParameters(int index) {
        if(intMatrixParameters.length>0 && index < intMatrixParameters.length)
                return intMatrixParameters[index];
        else
           throw new IllegalArgumentException();
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

    public boolean getAddOne() {
        if(this.addOne.isPresent())
            return addOne.get();
        else
            throw new IllegalArgumentException();

    }

    public ToolNames getToolName() {
        if(this.toolName.isPresent())
            return toolName.get();
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

    public String toString(){
        switch (typeMove){
            case "PlaceDie":
                return "dado:" + this.indexDie + " " + "coordinate:" + this.intMatrixParameters[0] + this.intMatrixParameters[1];
            case "GoThrough":
                return "go through";
            case "UseTool":
                return "use tool";
            default:
                return "default";
        }

    }
}


