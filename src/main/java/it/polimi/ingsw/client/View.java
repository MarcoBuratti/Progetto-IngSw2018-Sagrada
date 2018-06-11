package it.polimi.ingsw.client;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.client.interfaces.GraphicsInterface;
import it.polimi.ingsw.client.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.socket.SocketConnectionClient;
import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer, GraphicsInterface {
    private BufferedReader bufferedReader;
    private ClientInterface connectionClient;
    private boolean hasChosenScheme;
    private String nickname;
    private String schemes;
    private GraphicsClient graphicsClient;
    private String address;
    private String port;
    private String choice;
    private String fromClient;
    private String move;
    private String tmpMove;
    private String index;
    private String RowColumn;
    private String toolIndex;
    private boolean moveCtrl;
    private boolean connectionType;
    private boolean inputCtrl;
    private boolean toolCtrl = true;
    private boolean gameStarted = false;
    private InputController cliController;


    public View(InputStreamReader input) {
        bufferedReader = new BufferedReader(input);
        graphicsClient = new GraphicsClient();
        cliController = new InputController();
    }

    public void start() {


            setNickname();
            setIP();
            setPort();
            int portInt = Integer.parseInt(port);
            setChoice();
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

            if(connectionType){
                synchronized (this){
                    try {
                        wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }

            if (!hasChosenScheme) {
                setScheme();
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }

            while (connectionClient.getIsOn()) {
                if (hasChosenScheme) {
                    inputCtrl = true;
                    StringBuilder bld = new StringBuilder();
                    do {
                        setFirstInput();
                        bld.append(tmpMove);
                        if (tmpMove.equals("3") || tmpMove.equals("4")) {
                            moveCtrl = false;
                            inputCtrl = false;
                        }
                        if (tmpMove.equals("1")) {
                            System.out.println(graphicsClient.printRulesDash());
                            setIndexDash();
                            bld.append(" " + index);
                            System.out.println(graphicsClient.printRulesMatrix());
                            setRowColumn();
                            bld.append(" " + RowColumn);
                            inputCtrl = false;
                        }
                        if (tmpMove.equals("2")) {
                            bld.append(tmpMove);
                            System.out.println(graphicsClient.printToolIndex());
                            setSecondTool();
                            bld.append(" " + toolIndex);
                        }
                    } while (inputCtrl);
                    move = bld.toString();
                    System.out.println(move);
                    connectionClient.handleMove(move);
                }
            }

            System.out.println( graphicsClient.printEnd() );
    }

    private void setNickname(){
    try {
        inputCtrl = false;
        do {
            System.out.println(graphicsClient.askNick());
            nickname = bufferedReader.readLine();
            inputCtrl = cliController.nameController(nickname);
            if (inputCtrl) System.out.println(graphicsClient.wrongNick());
        } while (inputCtrl);
    }catch (IOException e) {
        System.err.println(e.toString());
    }
    }

    private void setIP() {
        try {
            do {
                System.out.println(graphicsClient.printIP());
                address = bufferedReader.readLine();
                inputCtrl = cliController.ipController(address);
            } while (inputCtrl);
        }catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private void setPort() {
        try {
            inputCtrl = true;
            do {
                System.out.println(graphicsClient.printPort());
                port = bufferedReader.readLine();
                inputCtrl = cliController.portController(port);
            } while (inputCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private  void setChoice() {
        try {
            inputCtrl = true;
            do {
                System.out.println(graphicsClient.printConnection());
                choice = bufferedReader.readLine();
                inputCtrl = cliController.connectionController(choice);
            } while (inputCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void setScheme() {
        try {
            inputCtrl = true;
            do {
                fromClient = bufferedReader.readLine();
                inputCtrl = cliController.schemeController(fromClient);
                if (inputCtrl) System.out.println(graphicsClient.printRequest());
            } while (inputCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void setFirstInput() {
        try {
            moveCtrl = true;
            do {
                tmpMove = bufferedReader.readLine();
                moveCtrl = this.connectionClient.firstInput(tmpMove);
                if (moveCtrl) System.out.println(graphicsClient.printRulesFirst());
            } while (moveCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void setIndexDash() {
        try {
            moveCtrl = true;
            do {
                index = bufferedReader.readLine();
                moveCtrl = this.connectionClient.secondInputDie(index);
                if (moveCtrl) System.out.println(graphicsClient.printRulesDash());
            } while (moveCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void setRowColumn() {
        try {
            moveCtrl = true;
            do {
                RowColumn = bufferedReader.readLine();
                moveCtrl = this.connectionClient.thirdInputDie(RowColumn);
                if (moveCtrl) System.out.println(graphicsClient.printRulesMatrix());
            } while (moveCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void setSecondTool(){
        try {
            moveCtrl = true;
            do {
                toolIndex = bufferedReader.readLine();
                moveCtrl = this.connectionClient.secondInputTool(toolIndex);
                if (moveCtrl) System.out.println(graphicsClient.printToolIndex());
            } while (moveCtrl);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    public synchronized void setHasChosenScheme(boolean bool) {
        this.hasChosenScheme = bool;
        notifyAll();
    }

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
                System.out.println(graphicsClient.printGeneric(fromServer));
            } else if (fromServer.startsWith("schemes. ")) {
                graphicsClient.printChoice(fromServer);
                schemes = fromServer;
                setHasChosenScheme(false);
            } else if (fromServer.startsWith("Your private achievement is:")) {
                System.out.println(graphicsClient.printPrivate(fromServer));
            } else if (fromServer.startsWith("Tools:")) {
                if (toolCtrl) {
                    this.connectionClient.setTool(fromServer);
                    toolCtrl = false;
                }
                graphicsClient.printTool(fromServer);
            } else if (fromServer.startsWith("UpdateFromServer")) {
                System.out.println( graphicsClient.printRulesFirst());
            }
            else if(fromServer.startsWith("The game has started!")) {
                gameStarted = true;
                System.out.println(graphicsClient.printGeneric(fromServer));
            }
            else {
                System.out.println(graphicsClient.printGeneric(fromServer));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
