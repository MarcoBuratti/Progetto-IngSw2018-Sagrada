package it.polimi.ingsw.server;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

class PortParser {

    private static final JSONParser parser = new JSONParser();

    /**
     * Private constructor that throws an IllegalStateException when called.
     * This is a static class.
     */
    private PortParser () {
        throw new IllegalStateException();
    }

    /**
     * A static method used to read the Server's ports from a json file and set them.
     * @param server the Server
     */
    static void setPorts(Server server) {
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/ports/Ports.json"));

            long socketPortLong = (Long) jsonObject.get("SocketPort");
            long rmiPortLong = (Long) jsonObject.get("RmiPort");
            int socketPort = (int) socketPortLong;
            int rmiPort = (int) rmiPortLong;

            server.setPorts(socketPort, rmiPort);
        }
        catch (IOException | ParseException e) {
            server.setPorts(1996, 1997);
        }
    }

}
