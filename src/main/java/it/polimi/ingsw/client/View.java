package it.polimi.ingsw.client;

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
    private Boolean hasChosenScheme;
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
    private boolean moveCtrl;
    private boolean connectionType;
    private boolean inputCtrl;
    private boolean toolCtrl = true;
    private InputController cliController;


    public View(InputStreamReader input) {
        bufferedReader = new BufferedReader(input);
        graphicsClient = new GraphicsClient();
        cliController = new InputController();
    }

    public void start() {
        try {

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

            if (connectionType) {
                synchronized (this) {
                    wait();
                }
            }
            if (!hasChosenScheme) {
                setScheme();
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }

            while (connectionClient.getIsOn()) {
                inputCtrl = true;
                do{
                    setFirstInput();
                    if( tmpMove.equals("3")  || tmpMove.equals("4") ){
                        move = tmpMove;
                        moveCtrl = false;
                        inputCtrl = false;
                    }
                    if( tmpMove.equals("1") ) {
                        setIndexDash();
                        tmpMove = tmpMove + " " + index;

                        setRowColumn();
                        tmpMove = tmpMove + " " + RowColumn;
                        inputCtrl = false;
                    }
                    if( tmpMove.equals("2") ){
                        //TODO implementare parte per tool
                    }
                }while (inputCtrl);
                move = tmpMove;
                connectionClient.handleMove(move);
            }

            System.out.println( graphicsClient.printEnd() );
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void setNickname() throws IOException {
        inputCtrl = false;
        do {
            System.out.println( graphicsClient.askNick() );
            nickname = bufferedReader.readLine();
            inputCtrl = cliController.nameController(nickname);
            if(inputCtrl) System.out.println( graphicsClient.wrongNick() );
        } while (inputCtrl);
    }

    private void setIP() throws IOException {
        do {
            System.out.println( graphicsClient.printIP() );
            address = bufferedReader.readLine();
            inputCtrl = cliController.ipController(address);
        } while (inputCtrl);
    }

    private void setPort() throws IOException {
        inputCtrl = true;
        do {
            System.out.println( graphicsClient.printPort() );
            port = bufferedReader.readLine();
            inputCtrl = cliController.portController(port);
        } while (inputCtrl);
    }

    private  void setChoice() throws IOException {
        inputCtrl = true;
        do {
            System.out.println( graphicsClient.printConnection() );
            choice = bufferedReader.readLine();
            inputCtrl = cliController.connectionController(choice);
        } while (inputCtrl);
    }

    private void setScheme() throws IOException {
        inputCtrl = true;
        do {
            fromClient = bufferedReader.readLine();
            inputCtrl = cliController.schemeController(fromClient);
            if(inputCtrl) System.out.println( graphicsClient.printRequest() );
        }while (inputCtrl);
    }

    private void setFirstInput() throws IOException {
        moveCtrl = true;
        do {
            tmpMove = bufferedReader.readLine();
            moveCtrl = this.connectionClient.firstInput(tmpMove);
            if(inputCtrl) System.out.println( graphicsClient.printRulesFirst() );
        }while (moveCtrl);
    }

    private void setIndexDash() throws IOException{
        moveCtrl = true;
        while (moveCtrl) {
            System.out.println( graphicsClient.printRulesDash() );
            index = bufferedReader.readLine();
            moveCtrl = this.connectionClient.secondInputDie(index);
        }
    }

    private void setRowColumn() throws IOException{
        moveCtrl = true;
        while (moveCtrl){
            System.out.println( graphicsClient.printRulesMatrix() );
            RowColumn = bufferedReader.readLine();
            moveCtrl = this.connectionClient.secondInputDie(RowColumn);
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
            else {
                System.out.println(graphicsClient.printGeneric(fromServer));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
