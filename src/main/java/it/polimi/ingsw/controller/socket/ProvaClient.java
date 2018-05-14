package it.polimi.ingsw.controller.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProvaClient {
    public static void main (String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}
