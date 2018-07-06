package it.polimi.ingsw.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class TimeParser {

    private TimeParser() {
        throw new IllegalStateException();
    }

    private static long time = 90000;
    private static final JSONParser parser = new JSONParser();

    /**
     *
     * @param timerType
     * @return
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
