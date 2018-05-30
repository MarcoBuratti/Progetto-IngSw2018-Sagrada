package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.socket.SocketConnectionClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private BufferedReader bufferedReader;
    private ClientInterface connectionClient;
    private Boolean hasChosenScheme;
    private String nickname;
    private String schemes;


    public View(InputStreamReader input){
        bufferedReader = new BufferedReader(input);
    }



    public void start() {
        try {
            System.out.println("Please insert your nickname: ");
            String nickname = bufferedReader.readLine();
            System.out.println("Press 1 to use Socket, 2 to use RMI.");
            int choice = Integer.parseInt(bufferedReader.readLine());
            hasChosenScheme = false;

            if (choice == 1) {
                connectionClient = new SocketConnectionClient(this);
            }
           else {
                connectionClient = new RmiConnectionClient(this);
            }

            connectionClient.handleName(nickname);

            if(!hasChosenScheme) {
                String fromClient = bufferedReader.readLine();
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }

           while (connectionClient.getIsOn()) {
                while (hasChosenScheme) {
                    String move = bufferedReader.readLine();
                    connectionClient.handleMove(move);
                }
            }

            System.out.println("Game ended.");
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
            else if (fromServer.startsWith("Please choose one of these schemes")) {
                System.out.println(fromServer);
                schemes = fromServer;
                hasChosenScheme = false;
            }

            else {
                System.out.println(fromServer);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
