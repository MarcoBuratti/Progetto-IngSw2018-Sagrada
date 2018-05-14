package it.polimi.ingsw.controller.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<String> usernames;
    protected static final int PORT = 1234;

    public Server (){
        try {
            serverSocket = new ServerSocket(PORT);
            usernames = new ArrayList<>();
            System.out.println("Server is online");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public ArrayList<String> getUsernames() {
        return (ArrayList) usernames.clone();
    }

    public void addUsername (String username) {
        this.usernames.add(username);
    }


    public void run () {
        Boolean b = true;
        while (b) {
            try {
                this.socket = serverSocket.accept();
            } catch (IOException e){
                System.out.println(e.toString());
                b=false;
            }
            new ServerToClientThread(this, socket).start();
        }
    }
}
