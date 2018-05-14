package it.polimi.ingsw.controller.socket;


import java.io.*;
import java.net.*;

public class ServerToClientThread extends Thread {
    private Socket socket;
    private BufferedReader fromClient;
    private PrintStream toClient;

    public ServerToClientThread(Socket socket) {
        try {
            this.socket = socket;
            fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toClient = new PrintStream(socket.getOutputStream());
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    private void signIn () {
        String message = "";
        try {
            toClient.println("Welcome to Sagrada. Please insert nickname here.");
            message = fromClient.readLine();
            System.out.println("New user has signed in as " + message);
            toClient.println("Now you're logged as " + message);
        }catch (IOException e) {
            System.out.println("IOException in ServerToClientThread");
        }
    }

    @Override
    public void run () {
        try{
            this.signIn();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}