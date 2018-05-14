package it.polimi.ingsw.controller.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader fromServer;
    private PrintStream toServer;

    public Client() {
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), Server.PORT);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toServer = new PrintStream(socket.getOutputStream());
            System.out.println("Client is online");
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public synchronized void run(){
        String message;
        String username;
        String chatMessage;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            message = fromServer.readLine();
            System.out.println(message);
            do {
                username = br.readLine();
                toServer.println(username);
                toServer.flush();
                message = fromServer.readLine();
                System.out.println(message);
            } while (!message.equals("Now you're logged as " + username));
            message = fromServer.readLine();
            System.out.println(message);
            chatMessage = br.readLine();
            while (!chatMessage.equals("/quit")) {
                toServer.println(chatMessage);
                toServer.flush();
                message = fromServer.readLine();
                System.out.println(message);
                chatMessage = br.readLine();
            }
            toServer.println(chatMessage);
            toServer.flush();
            System.out.println("You've been disconnected");
            this.socket.close();

        } catch (IOException e){
            System.out.println("IOException in Client");
        }
    }
}
