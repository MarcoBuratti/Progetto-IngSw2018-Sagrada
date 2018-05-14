package it.polimi.ingsw.controller.socket;


import java.io.*;
import java.net.*;

public class ServerToClientThread extends Thread {
    private Server server;
    private Socket socket;
    private BufferedReader fromClient;
    private PrintStream toClient;

    public ServerToClientThread(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toClient = new PrintStream(socket.getOutputStream());
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    private synchronized void signIn () {
        String message = "";
        String chatMessage;
        try {
            toClient.println("Welcome to Sagrada. Please insert nickname here.");
            toClient.flush();
            message = fromClient.readLine();
            String username = message;
            while(server.getUsernames().contains(username)){
                toClient.println("This nickname is already used by another user. Please insert another one.");
                toClient.flush();
                username = fromClient.readLine();
            }
            server.addUsername(username);
            System.out.println("New user has logged in as " + username);
            toClient.println("Now you're logged as " + username);
            toClient.flush();
            toClient.println("Insert your first message here:");
            toClient.flush();
            chatMessage = fromClient.readLine();
            while (!chatMessage.equals("/quit")){
                toClient.println(username + " writes: " + chatMessage);
                toClient.flush();
                System.out.println(username + " writes: " + chatMessage);
                chatMessage = fromClient.readLine();
            }
            System.out.println(username + " has disconnected from the server.");
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