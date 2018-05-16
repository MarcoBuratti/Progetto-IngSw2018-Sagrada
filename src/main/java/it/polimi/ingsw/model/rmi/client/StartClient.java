package it.polimi.ingsw.model.rmi.client;


import it.polimi.ingsw.model.rmi.interfaces.ChatInterface;
import it.polimi.ingsw.model.rmi.interfaces.MessengerInterface;

import java.rmi.Naming;
import java.util.Scanner;

public class StartClient {

    public static void main(String [] args){
        try {
            String chatServerURL = "127.0.0.1";
            ChatInterface chatServer = (ChatInterface) Naming.lookup(chatServerURL);
            ChatInterface server = (ChatInterface) Naming.lookup("1099");
            Scanner scanner = new Scanner(System.in);
            System.out.println("[System] Client Messenger is running\n");
            System.out.println("Enter a username to login and press enter: \n");
            String usernamen = scanner.nextLine();
            MessengerInterface m = new Messenger(usernamen, server);
            server.login(m);
            server.sendToAll("Just Connected", m);
            while (true){
                String newmex = scanner.nextLine();
                server.sendToAll(newmex, m);
            }
        }catch (Exception e) {
            System.out.println("Hello Client Exception: " + e);
            e.printStackTrace();
        }
    }
}
