package it.polimi.ingsw.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket server;
    private Socket connection;
    private BufferedReader fromClient;
    private PrintStream toClient;
    private Map<String, String> users = new HashMap<>();
    protected static final int PORT = 1234;

    public Server (){
        try {
            server = new ServerSocket(PORT, 4);
            System.out.println("Server is online");
            connection = server.accept();
            fromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            toClient = new PrintStream(connection.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void signIn () {
        String nickname = "";
        String newPsw = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            toClient.println("Welcome to Sagrada. Please insert nickname and password to sign in.");
            while (!nickname.equals("end")) {
                nickname = fromClient.readLine();
                toClient.println("Your nickname is " + nickname);
            }
            connection.close();
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
