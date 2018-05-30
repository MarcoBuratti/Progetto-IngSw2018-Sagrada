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
    private boolean firstLog;
    private boolean isOn;
    private boolean gameStarted;

    public SocketConnectionServer(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        firstLog = true;
        isOn = true;
    }

    private synchronized boolean getIsOn() {
        return isOn;
    }

    public synchronized void setOff(){
        send("You've been disconnected successfully.");
        this.isOn = false;
    }


    private synchronized void read (String jsonContent){
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
        bld.append(server.getSchemes().get(0).getFirstScheme());
        bld.append(",");
        bld.append(server.getSchemes().get(0).getSecondScheme());
        bld.append(",");
        server.getSchemes().remove(0);
        bld.append(server.getSchemes().get(0).getFirstScheme());
        bld.append(",");
        bld.append(server.getSchemes().get(0).getSecondScheme());
        String message = bld.toString();
        server.getSchemes().remove(0);
        String privateAchievement = server.getPrivateAchievements().get(0).toString();
        this.send("This is your private achievement: " + privateAchievement);
        this.send("Please choose one of these schemes: insert a number between 1 and 4. " + message);
        return in.readLine();
    }

    @Override
    public void run() {
        try {
            this.player = new Player(in.readLine(), this);
            firstLog = !server.alreadyLoggedIn(this);
            gameStarted = server.isGameStarted();
            server.registerConnection(this);
            if(firstLog && !gameStarted) {
                String chosenScheme = askForChosenScheme();
                this.player.setDashboard(chosenScheme);
                this.send("You have chosen the following scheme: " + chosenScheme + "\n" + this.player.getDashboard().toString() + "\nPlease wait, the game will start soon.");
            }
            isOn = true;

            while(isOn) {
                String message = in.readLine();
                if (message.equals("/quit")) {
                    setOff();
                    close();
                } else {
                    read(message);
                    PlayerMove newMove = PlayerMove.PlayerMoveConstructor();
                    send("Trying to make the following move: " + newMove.toString() + " ...");
                    setChanged();
                    notifyObservers(newMove);
                }
            }
        } catch (IOException | NotValidValueException e) {
            System.out.println("Connection expired.");
            this.send("Terminate.");
            close();
        }
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
    public void send(String message) {
        out.println(message);
        out.flush();
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
        server.deregisterConnection(this);
    }

}