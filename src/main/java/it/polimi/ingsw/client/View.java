package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.CliController;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.GraphicsInterface;
import it.polimi.ingsw.client.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.socket.SocketConnectionClient;
import it.polimi.ingsw.util.CliGraphicsClient;
import it.polimi.ingsw.util.CliInputController;

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
    private String address;
    private String port;
    private int portInt;
    private String choice;
    private int choiceInt;
    private String fromClient;
    private String move;
    private String tmpMove;
    private boolean connectionType; //true for socket, false for rmi
    private boolean inputCtrl;
    private CliController cliController;


    public View(InputStreamReader input) {
        bufferedReader = new BufferedReader(input);
    }

    public void start() {
        try {
            graphicsInterface = new CliGraphicsClient();
            cliController = new CliInputController();
            graphicsInterface.start();
            inputCtrl = true;
            do {
                graphicsInterface.insert();
                nickname = bufferedReader.readLine();
                inputCtrl = cliController.nameController(nickname);
            } while (inputCtrl);
            inputCtrl = true;
            do {
                graphicsInterface.printIP();
                address = bufferedReader.readLine();
                inputCtrl = cliController.ipController(address);
            } while (inputCtrl);
            inputCtrl = true;
            do {
                graphicsInterface.printPort();
                port = bufferedReader.readLine();
                inputCtrl = cliController.portController(port);
            } while (inputCtrl);
            portInt = Integer.parseInt(port);
            inputCtrl = true;
            do {
                graphicsInterface.printConnection();
                choice = bufferedReader.readLine();
                inputCtrl = cliController.connectionController(choice);
            } while (inputCtrl);
            choiceInt = Integer.parseInt(choice);
            hasChosenScheme = true;

            if (choiceInt == 1) {
                connectionType = true;
                connectionClient = new SocketConnectionClient(this, address, portInt);
            } else {
                connectionType = false;
                connectionClient = new RmiConnectionClient(this, address, portInt);
            }

            connectionClient.handleName(nickname);

            if (connectionType) {
                synchronized (this) {
                    wait();
                }
            }
            if (!hasChosenScheme) {
                inputCtrl = true;
                int i = 0;
                do {
                    if(i >= 1) graphicsInterface.printChoice(i);
                    fromClient = bufferedReader.readLine();
                    inputCtrl = cliController.schemeController(fromClient);
                    i++;
                }while (inputCtrl);
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }

            while (connectionClient.getIsOn()) {
                /*inputCtrl = true;
                do{
                    tmpMove = bufferedReader.readLine();
                }while (inputCtrl);*/
                move = bufferedReader.readLine();
                connectionClient.handleMove(move);
            }

            System.out.println("Game ended.");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    private synchronized void setHasChosenScheme(boolean bool) {
        this.hasChosenScheme = bool;
        notifyAll();
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        try {
            String fromServer = (String) arg;
            if (fromServer.startsWith("You have logged in")) {
                int nicknameStartIndex = fromServer.lastIndexOf(' ') + 1;
                this.nickname = fromServer.substring(nicknameStartIndex);
                this.connectionClient.setPlayerNickname(nickname);
                if (fromServer.startsWith("You have logged in again as")) {
                    setHasChosenScheme(true);
                }
                graphicsInterface.printGeneric(fromServer);
            } else if (fromServer.startsWith("schemes. ")) {
                graphicsInterface.printChoice(fromServer);
                schemes = fromServer;
                setHasChosenScheme(false);
            } else if (fromServer.startsWith("Your private achievement is:"))
                graphicsInterface.printPrivate(fromServer);
            else if (fromServer.startsWith("Tools:")) {
                this.connectionClient.setTool(fromServer);
                graphicsInterface.printTool(fromServer);
            }
            else if (fromServer.startsWith("UpdateFromServer"))
                graphicsInterface.printRules();
            else {
                graphicsInterface.printGeneric(fromServer);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
