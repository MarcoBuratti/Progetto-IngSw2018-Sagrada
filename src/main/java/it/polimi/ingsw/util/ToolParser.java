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

    private static ClientTool[] clientTools = new ClientTool[3];
    ;

    /**
     * Private constructor that throws an IllegalStateException when called.
     * This is a static class.
     */
    private ToolParser() {
        throw new IllegalStateException();
    }

    /**
     * A static method used to read information about the extracted ToolCards from a json file.
     *
     * @param s a String containing the tools' names
     * @return an array of ClientTool Objects
     */
    static synchronized ClientTool[] readTools(String s) {


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
                JSONArray move = (JSONArray) jsonObject.get("Move");

                for (int j = 0; j < param; j++) {
                    toolMove.add(TypeMove.valueOf((String) (move.get(j))));
                }

                clientTools[k] = new ClientTool(number, toolMove);

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return clientTools.clone();

    }

}
