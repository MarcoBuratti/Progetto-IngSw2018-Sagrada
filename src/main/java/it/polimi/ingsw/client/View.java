package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.CliController;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.CliOutPut;
import it.polimi.ingsw.client.interfaces.GraphicsInterface;
import it.polimi.ingsw.client.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.socket.SocketConnectionClient;
import it.polimi.ingsw.util.CliGraphicsClient;
import it.polimi.ingsw.util.CliInputController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer, GraphicsInterface {
    private BufferedReader bufferedReader;
    private ClientInterface connectionClient;
    private Boolean hasChosenScheme;
    private String nickname;
    private String schemes;
    private CliOutPut cliOutPut;
    private String address;
    private String port;
    private String choice;
    private String fromClient;
    private String move;
    private String tmpMove;
    private String index;
    private String RowColumn;
    private boolean moveCtrl;
    private boolean connectionType;
    private boolean inputCtrl;
    private boolean toolCtrl = true;
    private CliController cliController;


    public View(InputStreamReader input) {
        bufferedReader = new BufferedReader(input);
    }

    public void start() {
        try {
            cliOutPut = new CliGraphicsClient();
            cliController = new CliInputController();
            cliOutPut.start();

            nickname = setNickname();
            address = setIP();
            port = setPort();
            int portInt = Integer.parseInt(port);
            choice = setChoice();
            int choiceInt = Integer.parseInt(choice);

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
                fromClient = setScheme();
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }

            while (connectionClient.getIsOn()) {
                inputCtrl = true;
                do{
                    tmpMove = setFirstInput();
                    if(Integer.parseInt(tmpMove) == 3 || Integer.parseInt(tmpMove) == 4){
                        move = tmpMove;
                        moveCtrl = false;
                        inputCtrl = false;
                    }
                    if(Integer.parseInt(tmpMove) == 1) {
                        index = setIndexDash();
                        tmpMove = tmpMove + " " + index;

                        RowColumn = setRowColumn();
                        tmpMove = tmpMove + " " + RowColumn;
                        inputCtrl = false;
                    }
                    if(Integer.parseInt(tmpMove) == 2){
                        //TODO implementare parte per tool
                    }
                }while (inputCtrl);

                move = tmpMove;
                connectionClient.handleMove(move);
            }

            System.out.println("Game ended.");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public synchronized String setNickname() throws IOException {
        inputCtrl = true;
        do {
            cliOutPut.insert();
            nickname = bufferedReader.readLine();
            inputCtrl = cliController.nameController(nickname);
        } while (inputCtrl);
        return nickname;
    }

    public synchronized String setIP() throws IOException {
        do {
            cliOutPut.printIP();
            address = bufferedReader.readLine();
            inputCtrl = cliController.ipController(address);
        } while (inputCtrl);
        return address;
    }

    public synchronized String setPort() throws IOException {
        inputCtrl = true;
        do {
            cliOutPut.printPort();
            port = bufferedReader.readLine();
            inputCtrl = cliController.portController(port);
        } while (inputCtrl);
        return port;
    }

    public synchronized String setChoice() throws IOException {
        inputCtrl = true;
        do {
            cliOutPut.printConnection();
            choice = bufferedReader.readLine();
            inputCtrl = cliController.connectionController(choice);
        } while (inputCtrl);
        return choice;
    }

    public synchronized String setScheme() throws IOException {
        inputCtrl = true;
        int i = 0;
        do {
            if(i >= 1) cliOutPut.printChoice(i);
            fromClient = bufferedReader.readLine();
            inputCtrl = cliController.schemeController(fromClient);
            i++;
        }while (inputCtrl);
        return fromClient;
    }

    public String getNickname() {
        return this.nickname;
    }

    public synchronized void setHasChosenScheme(boolean bool) {
        this.hasChosenScheme = bool;
        notifyAll();
    }

    public synchronized String setFirstInput() throws IOException {
        moveCtrl = true;
        while (moveCtrl){
            cliOutPut.printRulesFirst();
            tmpMove = bufferedReader.readLine();
            moveCtrl = this.connectionClient.firstInput(tmpMove);
        }
        return tmpMove;
    }

    public synchronized String setIndexDash() throws IOException{
        moveCtrl = true;
        while (moveCtrl) {
            cliOutPut.printRulesDash();
            index = bufferedReader.readLine();
            moveCtrl = this.connectionClient.secondInputDie(index);
        }
        return index;
    }

    public synchronized String setRowColumn() throws IOException{
        moveCtrl = true;
        while (moveCtrl){
            cliOutPut.printRulesMatrix();
            RowColumn = bufferedReader.readLine();
            moveCtrl = this.connectionClient.secondInputDie(RowColumn);
        }
        return RowColumn;
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
                cliOutPut.printGeneric(fromServer);
            } else if (fromServer.startsWith("schemes. ")) {
                cliOutPut.printChoice(fromServer);
                schemes = fromServer;
                setHasChosenScheme(false);
            } else if (fromServer.startsWith("Your private achievement is:")) {
                cliOutPut.printPrivate(fromServer);
            }
            else if (fromServer.startsWith("Tools:")) {
                if(toolCtrl){
                    this.connectionClient.setTool(fromServer);
                    toolCtrl = false;
                }
                cliOutPut.printTool(fromServer);
            }
            else if(fromServer.startsWith("UpdateFromServer") )
                cliOutPut.printRulesFirst();
            else {
                cliOutPut.printGeneric(fromServer);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
