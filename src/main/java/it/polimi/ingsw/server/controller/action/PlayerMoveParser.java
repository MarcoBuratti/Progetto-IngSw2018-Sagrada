package it.polimi.ingsw.server.controller.action;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

class PlayerMoveParser {

    private static final JSONParser parser = new JSONParser();

    /**
     * Private constructor that throws an IllegalStateException when called.
     * This is a static class.
     */
    private PlayerMoveParser() {
        throw new IllegalStateException();
    }

    /**
     * Allows the user to read a new move from a json file, parsing it into a new JSONObject.
     *
     * @return a JSONObject containing the new move
     */
    static synchronized JSONObject readMove() {
        try {
            return (JSONObject) parser.parse(new FileReader("src/main/files/LastPlayerMove.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException();
    }
}
