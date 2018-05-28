package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.rmi.RmiMessageHandler;
import it.polimi.ingsw.client.socket.SocketConnectionClient;
import it.polimi.ingsw.client.socket.SocketMessageHandler;
import it.polimi.ingsw.util.MessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

public class View implements Observer {
    private BufferedReader bufferedReader;
    private ClientInterface connectionClient;
    private MessageHandler messageHandler;
    private Boolean hasChosenScheme;
    private String nickname;


    public View(InputStreamReader input){
        bufferedReader = new BufferedReader(input);
    }



    public void start() {
        try {
            System.out.println("Please insert your nickname: ");
            String nickname = bufferedReader.readLine();
            System.out.println("Premi 1 per Socket, 2 per RMI");
            int choice = Integer.parseInt(bufferedReader.readLine());
            hasChosenScheme = false;

            if (choice == 1) {
                connectionClient = new SocketConnectionClient(this);
                messageHandler = new SocketMessageHandler(connectionClient);
            }
           else {
                connectionClient = new RmiConnectionClient(this);
                messageHandler = new RmiMessageHandler(connectionClient);
            }

            messageHandler.handleName(nickname);


            System.out.println("Premi -1 per terminare la partita");

            while (connectionClient.getIsOn()) {
                while (hasChosenScheme) {
                    String move = bufferedReader.readLine();
                    messageHandler.handleMove(move);
                }
            }
            System.out.println("Partita terminata");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public String getNickname(){
        return this.nickname;
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        try {
            String fromServer = (String) arg;
            if (fromServer.startsWith("You have logged in")){
                int nicknameStartIndex = fromServer.lastIndexOf(' ') + 1;
                this.nickname = fromServer.substring(nicknameStartIndex);
                this.connectionClient.setPlayerNickname(nickname);
                if (fromServer.startsWith("You have logged in again as")) {
                    hasChosenScheme = true;
                }
                System.out.println(fromServer);
            }
            else if (fromServer.startsWith("Please choose one of these schemes in a minute: insert a number between 1 and 4.")) {
                System.out.println(fromServer);
                String fromClient = bufferedReader.readLine();
                messageHandler.handleScheme(fromServer, fromClient);
                hasChosenScheme = true;
            }

            else
                System.out.println(fromServer);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
