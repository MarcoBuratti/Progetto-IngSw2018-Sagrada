package it.polimi.ingsw.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class TimeParser {

    private static long time = 90000;
    private static final JSONParser parser = new JSONParser();

    /**
     * Private constructor that throws an IllegalStateException when called.
     * This is a static class.
     */
    private TimeParser() {
        throw new IllegalStateException();
    }

    /**
     * A static method used to read the time used for timers from a json file and set it.
     * If it's impossible to read from the selected json file, it return a default timer value (90000ms).
     * @param timerType a String used to select the correct json file
     * @return a long representing the time in ms used for a timer
     */
    public static synchronized long readTime ( String timerType ) {

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/timers/" + timerType + ".json"));

            time = (Long) jsonObject.get("Time");

            return time;
        }

        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

}
