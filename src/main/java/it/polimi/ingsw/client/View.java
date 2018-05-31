package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.GraphicsInterface;
import it.polimi.ingsw.client.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.socket.SocketConnectionClient;
import it.polimi.ingsw.util.CliGraphicsClient;

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
    private GraphicsInterface graphicsInterface;
    private boolean connectionType; //true for socket, false for rmi


    public View(InputStreamReader input){
        bufferedReader = new BufferedReader(input);
    }

    public void start() {
        try {
            graphicsInterface = new CliGraphicsClient();
            graphicsInterface.start();
            graphicsInterface.insert();
            String nickname = bufferedReader.readLine();
            graphicsInterface.printConnection();
            int choice = Integer.parseInt(bufferedReader.readLine());
            hasChosenScheme = true;

            if (choice == 1) {
                connectionType = true;
                connectionClient = new SocketConnectionClient(this);
            }
            else {
                connectionType = false;
                connectionClient = new RmiConnectionClient(this);
            }

            connectionClient.handleName(nickname);
            System.out.println("view: dopo handle name");


            if (connectionType) {
                System.out.println("prima della wait");
                synchronized (this) {
                    System.out.println("sto attivando wait");
                    wait();
                }
            }

            System.out.println("dopo blocco wait, prima di controllo per handle scheme");
            System.out.println(hasChosenScheme);
            if(!hasChosenScheme) {
                String fromClient = bufferedReader.readLine();
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }

            System.out.println("prima del while");
           while (connectionClient.getIsOn()) {
               System.out.println("dentro il while, prima di readline");
                String move = bufferedReader.readLine();
               System.out.println("dopo readline");
                connectionClient.handleMove(move);
               System.out.println("dopo handle move");
            }

            System.out.println("Game ended.");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public String getNickname(){
        return this.nickname;
    }

    private synchronized void setHasChosenScheme (boolean bool){
        this.hasChosenScheme = bool;
        System.out.println("prima di notify all");
        notifyAll();
        System.out.println("dopo notify all");
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
                    setHasChosenScheme(true);
                }

                graphicsInterface.printGeneric(fromServer);
            }
            else if (fromServer.startsWith("Please choose one of these schemes")) {
                graphicsInterface.printGeneric(fromServer);
                schemes = fromServer;
                System.out.println("prima di settare hasChosenScheme");
                setHasChosenScheme(false);
                System.out.println("dopo aver settato hasChosenScheme");
            }

            else {
                graphicsInterface.printGeneric(fromServer);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
