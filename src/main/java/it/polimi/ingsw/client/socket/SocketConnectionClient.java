package it.polimi.ingsw.client.socket;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.interfaces.ClientInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketConnectionClient extends Observable implements Runnable, ClientInterface {


    private Socket socket;
    private BufferedReader in;
    private PrintStream out;
    private boolean isOn = true;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private String playerNickname;

    public SocketConnectionClient(View view) {
        this.addObserver(view);
        try {
            socket = new Socket(InetAddress.getByName(null), 1996);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            executor.submit(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized boolean getIsOn() {
        return isOn;
    }

    private synchronized void setOff() {
        isOn = false;
    }

    public void send(String message) {
        out.println(message);
    }



    public synchronized void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOff();
        executor.shutdown();

    }


    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    @Override
    public void run() {
        try {
            while(getIsOn()) {
                String message = in.readLine();
                setChanged();
                notifyObservers(message);
                if(message.equals("Terminate."))
                    close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}
