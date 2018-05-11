package it.polimi.ingsw.controller;


import java.io.*;
import java.net.*;

public class Client {
    private Socket connection;
    private BufferedReader fromServer;
    private PrintStream toServer;

    public Client() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Inserire indirizzo server: ");
            String address = br.readLine();
            connection = new Socket(address, Server.PORT);
            fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            toServer = new PrintStream(connection.getOutputStream());
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    public void signIn () {
        String message = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         try {
             while (!message.equals("end")) {
                message = fromServer.readLine();
                System.out.println(message);
                message = br.readLine();
                toServer.println(message);
            }
            connection.close();
         }catch (IOException e) {
             System.out.println("Conversazione interrotta");
         }
    }
}