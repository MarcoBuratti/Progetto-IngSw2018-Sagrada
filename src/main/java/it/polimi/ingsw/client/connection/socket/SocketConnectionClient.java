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

    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private String playerNickname;


    /**
     *
     * @param view
     * @param s
     * @param port
     */
    public SocketConnectionClient(View view, String s, int port) {
        this.addObserver(view);
        try {
            socket = new Socket(s, port);
            super.setView(view);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            executor.submit(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param message
     */
    private void send(String message) {
        out.println(message);
    }

    /**
     *
     */
    private synchronized void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setIsOn( false );
        executor.shutdown();

    }

    /**
     *
     * @param fromClient
     */
    private void placeDieHandler(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient, " ");
        strtok.nextToken();
        StringBuilder bld = new StringBuilder();
        bld.append("playerID ");
        bld.append(this.playerNickname);
        bld.append(" type_playerMove PlaceDie");
        int i = 1;

        while (strtok.hasMoreTokens()) {
            String key = "Key" + i;
            String value = strtok.nextToken();
            bld.append(" " + key + " " + value);
            i++;
        }

        this.send( bld.toString() );
    }

    /**
     *
     * @param fromClient
     */
    private void useToolHandler(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        strtok.nextToken();
        StringBuilder bld = new StringBuilder();
        bld.append("playerID ");
        bld.append(this.playerNickname);
        bld.append(" type_playerMove UseTool");
        int i = 1;
        String toolIndex = strtok.nextToken();
        bld.append( " toolIndex ");
        bld.append(toolIndex);
        toolIndex = strtok.nextToken();
        bld.append( " extractedToolIndex ");
        bld.append(toolIndex);


        while (strtok.hasMoreTokens()) {
            bld.append(" Key");
            bld.append(i);
            bld.append(" ");
            String parameter = strtok.nextToken();
            bld.append(parameter);
            i++;
        }

        this.send( bld.toString() );
    }

    /**
     *
     */
    private void goThroughHandler() {
        StringBuilder bld = new StringBuilder();
        bld.append("playerID ");
        bld.append(this.playerNickname);
        bld.append(" type_playerMove GoThrough");
        this.send( bld.toString() );
    }

    /**
     *
     */
    private void quitHandler() {
        send("/quit");
    }

    /**
     *
     * @param nickname the String the user wants to set as playerNickname attribute.
     */
    @Override
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    /**
     *
     * @param name a String specifying the chosen nickname
     */
    @Override
    public void handleName(String name) {
        setPlayerNickname(name);
        send(name);
    }

    /**
     *
     * @param fromServer a String containing the names of the available schemes
     * @param fromClient a String containing the client input
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
     *
     * @param fromClient a String containing the client input
     */
    @Override
    public void handleMove(String fromClient) {
        System.out.println(fromClient + "con spazio");
        fromClient = fromClient.substring(0, fromClient.length()-1);
        System.out.println(fromClient + "senza spazio");
        String  [] substringSchemes = fromClient.split(" ");
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

    @Override
    public void run() {
        try {
            while (getIsOn()) {
                String message = in.readLine();
                setChanged();
                notifyObservers(message);
                if (message.equals("Terminate."))
                    close();
                else if (message.equals("Please complete your move:"))
                    setWaitOn(false);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}