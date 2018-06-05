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

    @Override
    public synchronized boolean getIsOn() {
        return isOn;
    }

    private synchronized void setOff() {
        isOn = false;
    }

    private void send(String message) {
        out.println(message);
    }

    private synchronized void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOff();
        executor.shutdown();

    }


    @Override
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

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

    private void placeDieHandler(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient, " ");
        strtok.nextToken();
        StringBuilder bld = new StringBuilder();
        bld.append("playerID ");
        bld.append(this.playerNickname);
        String json_translation;
        bld.append(" type_playerMove PlaceDie");
        int i = 1;
        while (strtok.hasMoreTokens()) {
            String key = "Key" + i;
            String value = strtok.nextToken();
            bld.append(" " + key + " " + value);
            i++;
        }
        json_translation = bld.toString();
        send(json_translation);
    }

    private void goThroughHandler() {
        StringBuilder bld = new StringBuilder();
        bld.append("playerID ");
        bld.append(this.playerNickname);
        bld.append(" type_playerMove GoThrough");
        send(bld.toString());
    }

    private void quitHandler() {
        send("/quit");
    }

    private void useToolHandler(String fromClient) {
        StringTokenizer strtok = new StringTokenizer(fromClient);
        strtok.nextToken();
        StringBuilder bld = new StringBuilder();
        bld.append("playerID ");
        bld.append(this.playerNickname);
        String json_translation;
        bld.append(" type_playerMove UseTool");
        int i = 1;
        while (strtok.hasMoreTokens()) {
            //DA IMPLEMENTARE
        }
    }

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

    @Override
    public void handleName(String name) {
        send(name);
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
