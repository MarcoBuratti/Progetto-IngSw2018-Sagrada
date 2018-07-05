package it.polimi.ingsw.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ToolParser {

    private ToolClient [] toolClients;

    ToolParser(String s){

        toolClients = new ToolClient[3];
        String[] tool = s.split(",");

        JSONParser parser = new JSONParser();
        int k = 0;
        for (int i = 0; i < tool.length; i = i + 2, k++) {

            JSONObject jsonObject;

            try {

                jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/tool/" + tool[i] + ".json"));
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

                toolClients[k] = new ToolClient(message, number, toolMove);

            }

            catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

    }


    ToolClient[] getToolClients() {
        return toolClients;
    }
}
