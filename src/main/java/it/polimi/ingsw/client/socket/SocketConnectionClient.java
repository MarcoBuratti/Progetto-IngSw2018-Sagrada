package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Observable;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketConnectionClient extends Observable implements Runnable, ClientInterface {

    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private boolean isOn = true;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private String playerNickname;
    private String [] tool;

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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            executor.submit(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private synchronized void setOff() {
        isOn = false;
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
        setOff();
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
     * @return
     */
    @Override
    public synchronized boolean getIsOn() {
        return isOn;
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
        String substringSchemes = fromServer.substring(fromServer.indexOf(".") + 2);
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
        StringTokenizer strtok = new StringTokenizer(fromClient);
        int moveChoice = Integer.parseInt(strtok.nextToken());
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
     *
     */
    @Override
    public void setTool(String s) {
        s = s.substring(s.indexOf(":") + 2);
        tool = s.split(",");
    }

    @Override
    public boolean firstInput(String s) {
        if (!s.matches("[1-4]")) return true;
        else if (Integer.parseInt(s) > 0 || Integer.parseInt(s) <= 4) return false;
        else return true;
    }

    @Override
    public boolean secondInputDie(String s) {
        return false;
    }

    @Override
    public boolean thirdInputDie(String s) {
        return false;
    }

    @Override
    public boolean secondInputTool(String s) {
        return false;
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
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
