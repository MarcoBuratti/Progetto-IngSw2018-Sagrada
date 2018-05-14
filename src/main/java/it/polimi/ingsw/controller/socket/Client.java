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

    public void run(){
        String message = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            message = fromServer.readLine();
            System.out.println(message);
            message = br.readLine();
            toServer.println(message);
            message = fromServer.readLine();
            System.out.println(message);
        } catch (IOException e){
            System.out.println("IOException in Client");
        }
    }
}
