package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.socket.SocketConnectionClient;
import it.polimi.ingsw.client.socket.SocketMessageHandler;
import it.polimi.ingsw.util.MessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    BufferedReader bufferedReader;
    ClientInterface connectionClient;
    MessageHandler messageHandler;

    public View(InputStreamReader input){
        bufferedReader = new BufferedReader(input);
    }



    public void start() {
        try {
            System.out.println("Please insert your nickname: ");
            String nickname = bufferedReader.readLine();
            System.out.println("Premi 1 per Socket, 2 per RMI");
            int choice = Integer.parseInt(bufferedReader.readLine());

            if (choice == 1) {
                connectionClient = new SocketConnectionClient(this);
                messageHandler = new SocketMessageHandler(connectionClient);
            }
           /* else {
                connectionClient = new ConnectionRmiClient(this, player);
                messageHandler = new RmiMessageHandler(connectionClient);
            }*/

            messageHandler.handleName(nickname);


            System.out.println("Premi -1 per terminare la partita");

            while (connectionClient.getIsOn()) {
                String move = bufferedReader.readLine();
                messageHandler.handleMove(move);
            }
            System.out.println("Partita terminata");
        } catch (Exception e) {
            System.err.println(e.toString());
        }


    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            String fromServer = (String) arg;
            if (fromServer.startsWith("Please choose one of these schemes in a minute: insert a number between 1 and 4.")) {
                System.out.println(fromServer);
                String fromClient = bufferedReader.readLine();
                messageHandler.handleScheme(fromServer, fromClient);
            } else System.out.println(fromServer);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
