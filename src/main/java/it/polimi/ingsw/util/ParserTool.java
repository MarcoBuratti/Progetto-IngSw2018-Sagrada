package it.polimi.ingsw.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ParserTool {

    private ToolClient [] toolClients;

    public ParserTool(String s){
        toolClients = new ToolClient[3];
        String[] tool = s.split(",");
        JSONParser parser = new JSONParser();
        for (int i = 0; i < 3; i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/tool/" + tool[i] + ".json"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            String number = (String) jsonObject.get("Number");
            long param = (Long) jsonObject.get("Param");
            List<TypeMove> toolMove = new ArrayList<>();
            List<String> message = new ArrayList<>();
            JSONArray move = (JSONArray) jsonObject.get("Move");
            //JSONArray mes = (JSONArray) jsonObject.get("Message");
            for(int j = 0; j < param; j++) {
                toolMove.add(TypeMove.valueOf((String) (move.get(j))));
                //message.add( (String) mes.get(i));
            }

            toolClients[i] = new ToolClient(message, number, toolMove);
        }

    }


    public ToolClient[] getToolClients() {
        return toolClients;
    }
}
