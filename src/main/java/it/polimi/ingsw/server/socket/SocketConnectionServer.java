package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.StringTokenizer;

public class SocketConnectionServer extends Observable implements Runnable, ServerInterface {

    private Server server;
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;
    private Player player;
    private boolean isYourTurn;
    private boolean isOn;
    private MessageSender messageSender;

    public SocketConnectionServer(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        isYourTurn = false;
        isOn = true;
        messageSender = new MessageSender();
    }

    @Override
    public void send(String message) {
        out.println(message);
        out.flush();
    }

    public boolean getIsOn() {
        return isOn;
    }

    public void setOff(){
        this.isOn = false;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean getYourTurn() {
        return isYourTurn;
    }

    @Override
    public synchronized void setYourTurn(boolean bool) {
        this.isYourTurn = bool;
        notifyAll();
    }

    @Override
    public void close() {

        send("Connection expired.");
        send("Terminate.");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isYourTurn = false;
        server.deregisterConnection(this);
    }

    public synchronized void read (String jsonContent){
        StringTokenizer strtok = new StringTokenizer(jsonContent);
        String key, value;
        JSONObject jsonObject = new JSONObject();
        while(strtok.hasMoreTokens()){
            key = strtok.nextToken();
            value = strtok.nextToken();
            jsonObject.put(key, value);
        }
        try (FileWriter lastPlayerMove = new FileWriter("src/main/files/LastPlayerMove.json")) {
            lastPlayerMove.write(jsonObject.toJSONString());
        } catch (IOException e){
            System.out.println(e.toString());
        }
    }

    private synchronized String askForChosenScheme () throws IOException {
        StringBuilder bld = new StringBuilder();
        bld.append(server.getSchemes().get(0).getFirstScheme() + "," + server.getSchemes().get(0).getSecondScheme());
        bld.append(",");
        server.getSchemes().remove(0);
        bld.append(server.getSchemes().get(0).getFirstScheme() + "," + server.getSchemes().get(0).getSecondScheme());
        String message = bld.toString();
        server.getSchemes().remove(0);
        this.send("Please choose one of these schemes in a minute: insert a number between 1 and 4. " + message);
        String chosenScheme = in.readLine();
        return chosenScheme;
    }

    public void notYourTurn (){
        send("It's not your turn! Please wait.");
    }

    @Override
    public void run() {
        try {
            this.player = new Player(in.readLine(), this);
            server.registerConnection(this);
            if ( this.player.getDashboard() == null ) {
                String chosenScheme = askForChosenScheme();
                this.player.setDashboard(chosenScheme);
                this.send("You have chosen the following scheme: " + chosenScheme + "\n" + this.player.getDashboard().toString());
            }
            isOn = true;
            while(isOn) {
                synchronized (this) {
                    if (getYourTurn()) {
                        send("Make your move now.");
                        String message = in.readLine();
                        if (message.equals("/quit"))
                            setOff();
                        else {
                            read(message);
                            PlayerMove newMove = PlayerMove.PlayerMoveConstructor();
                            send(newMove.toString());
                            messageSender.send(newMove);
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                           /* setChanged();
                            notifyObservers(newMove);
                            */
                        }
                    } else {
                        send("Please wait your turn.");
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException | NotValidValueException e) {
            System.out.println("Connection expired.");
        } finally {
            server.deregisterConnection(this);
            this.send("Terminate.");
            close();
        }
    }


    private class MessageSender extends Observable {

        private void send(PlayerMove playerMove){
            setChanged();
            notifyObservers(playerMove);
        }

    }

    @Override
    public Observable getMessageSender() {
        return messageSender;
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean bool = (boolean) arg;
        setYourTurn(bool);
        if(bool == false)
            send("Your turn has ended.");
    }
}