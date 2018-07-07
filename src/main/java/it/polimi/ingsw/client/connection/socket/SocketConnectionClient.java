package it.polimi.ingsw.client.connection.socket;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.connection.ConnectionClient;
import it.polimi.ingsw.client.interfaces.ClientInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketConnectionClient extends ConnectionClient implements Runnable, ClientInterface {

    private static final String PLAYER_ID = "playerID ";
    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private String playerNickname;


    /**
     * Creates a SocketConnectionClient object, adding the corresponding View to its observers and establishing a connection between it and the server.
     *
     * @param view    the View object which has to be added to the observers
     * @param address the server address
     * @param port    the server port
     */
    public SocketConnectionClient(View view, String address, int port) {
        this.addObserver(view);
        try {
            socket = new Socket(address, port);
            super.setView(view);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            executor.submit(this);
            view.connectionSuccess();
        } catch (IOException e) {
            view.errorLogin();
        }

    }

    /**
     * Sends a message to the Server through the socket.
     *
     * @param message the message (String)
     */
    private void send(String message) {
        out.println(message);
    }

    /**
     * Sets the boolean attribute isOn as false whenever the connection needs to be closed
     * and closes the connection with the socket, shutting the executor down too.
     */
    private synchronized void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setIsOn(false);
        executor.shutdown();

    }

    /**
     * Creates a String containing the information the server needs to create a new placement move.
     *
     * @param fromClient the String sent by the client (through the view)
     */
    private void placeDieHandler(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient, " ");
        strtok.nextToken();
        StringBuilder bld = new StringBuilder();
        bld.append(PLAYER_ID);
        bld.append(this.playerNickname);
        bld.append(" type_playerMove PlaceDie");
        int i = 1;

        while (strtok.hasMoreTokens()) {
            String key = "Key" + i;
            String value = strtok.nextToken();
            bld.append(" ").append(key).append(" ").append(value);
            i++;
        }

        this.send(bld.toString());
    }

    /**
     * Creates a String containing the information the server needs to create a new tool move.
     *
     * @param fromClient the String sent by the client (through the view)
     */
    private void useToolHandler(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        strtok.nextToken();
        StringBuilder bld = new StringBuilder();
        bld.append(PLAYER_ID);
        bld.append(this.playerNickname);
        bld.append(" type_playerMove UseTool");
        int i = 1;
        String toolIndex = strtok.nextToken();
        bld.append(" toolIndex ");
        bld.append(toolIndex);
        toolIndex = strtok.nextToken();
        bld.append(" extractedToolIndex ");
        bld.append(toolIndex);


        while (strtok.hasMoreTokens()) {
            bld.append(" Key");
            bld.append(i);
            bld.append(" ");
            String parameter = strtok.nextToken();
            bld.append(parameter);
            i++;
        }

        this.send(bld.toString());
    }

    /**
     * Creates a String containing the information the server needs to create new go through move.
     */
    private void goThroughHandler() {
        String bld = PLAYER_ID +
                this.playerNickname +
                " type_playerMove GoThrough";
        this.send(bld);
    }

    /**
     * Sends to the Server the message that makes it close the connection.
     */
    private void quitHandler() {
        send("/quit");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleName(String name) {
        setPlayerNickname(name);
        send(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleScheme(String fromServer, String fromClient) {

        int choice = Integer.parseInt(fromClient);
        String substringSchemes = fromServer.substring(fromServer.indexOf('.') + 2);
        StringTokenizer strtok = new StringTokenizer(substringSchemes, ",");
        String[] schemes = new String[4];
        int i = 0;
        while (strtok.hasMoreTokens()) {
            schemes[i] = strtok.nextToken();
            i++;
        }
        String chosenScheme;
        if (choice > 0 && choice <= 4)
            chosenScheme = schemes[choice - 1];
        else
            chosenScheme = schemes[0];

        send(chosenScheme);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleMove(String fromClient) {

        fromClient = fromClient.substring(0, fromClient.length() - 1);
        String[] substringSchemes = fromClient.split(" ");
        int moveChoice = Integer.parseInt(substringSchemes[0]);
        switch (moveChoice) {
            case 1:
                placeDieHandler(fromClient);
                break;
            case 2:
                useToolHandler(fromClient);
                break;
            case 3:
                goThroughHandler();
                break;
            case 4:
                quitHandler();
                break;
            default:
                goThroughHandler();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            while (getIsOn()) {
                String message = in.readLine();
                if (!message.isEmpty()) {
                    setChanged();
                    notifyObservers(message);
                    if (message.equals("Terminate."))
                        close();
                    else
                        checkMessage(message);
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
