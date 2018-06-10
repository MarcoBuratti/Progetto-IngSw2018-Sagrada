package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.Game;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.action.PlayerMove;
import it.polimi.ingsw.server.interfaces.ServerInterface;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.achievement.PrivateAchievement;
import it.polimi.ingsw.server.model.exception.NotValidValueException;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.StringTokenizer;

public class SocketConnectionServer extends Observable implements Runnable, ServerInterface {

    private Server server;
    private Game game;
    private Socket socket;
    private PrintStream out;
    private BufferedReader in;
    private Player player;
    private boolean gameStarted = false;
    private boolean isOn;

    public SocketConnectionServer(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        isOn = true;
    }

    public synchronized void setOff() {
        send("You've been disconnected successfully.");
        this.isOn = false;
    }


    private synchronized void read(String jsonContent) {
        StringTokenizer strtok = new StringTokenizer(jsonContent);
        String key, value;
        JSONObject jsonObject = new JSONObject();
        while (strtok.hasMoreTokens()) {
            key = strtok.nextToken();
            value = strtok.nextToken();
            jsonObject.put(key, value);
        }
        try (FileWriter lastPlayerMove = new FileWriter("src/main/files/LastPlayerMove.json")) {
            lastPlayerMove.write(jsonObject.toJSONString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private synchronized String askForChosenScheme(String schemes) throws IOException {
        this.send("schemes. " + schemes);
        return in.readLine();
    }


    private String defaultScheme(String schemes) {
        StringTokenizer strtok = new StringTokenizer(schemes, ",");
        String defaultScheme = strtok.nextToken();
        try {
            this.player.setDashboard(defaultScheme);
        } catch (NotValidValueException e) {
            e.printStackTrace();
        }
        return defaultScheme;
    }

    @Override
    public void run() {
        try {
            this.player = new Player(in.readLine(), this);
            boolean firstLog = !server.alreadyLoggedIn(this);

            if ( firstLog ) {
                server.registerConnection(this);
                synchronized (this) {
                    while (!gameStarted) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                String schemes = game.selectSchemes();
                String defaultScheme = defaultScheme(schemes);
                Color privateAchievementColor = game.selectPrivateAchievement();
                this.player.setPrivateAchievement(new PrivateAchievement(privateAchievementColor));
                this.send("Your private achievement is: " + privateAchievementColor);
                String chosenScheme = askForChosenScheme(schemes);
                boolean schemeChosen = game.isSchemeChosen();
                if (!schemeChosen) {
                    this.player.setDashboard(chosenScheme);
                    this.send("You have chosen the following scheme: " + chosenScheme + "\n" + this.player.getDashboard().toString() + "\nPlease wait, the game will start soon.");
                } else
                    this.send("Too late! Your scheme is: " + defaultScheme + "\n" + this.player.getDashboard().toString() + "\nThe game has already started!");
            }

            else
                server.registerConnection(this);

            isOn = true;

            while (isOn) {
                String message = in.readLine();
                if (message.equals("/quit")) {
                    close();
                } else {
                    read(message);
                    PlayerMove newMove = PlayerMove.playerMoveConstructor();
                    send("Trying to make the following move: " + newMove.toString() + " ...");
                    setChanged();
                    notifyObservers(newMove);
                }
            }
        } catch (IOException | NotValidValueException e) {
            server.deregisterConnection(this);
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public synchronized void setGame(Game game) {
        this.game = game;
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

        setOff();
        send("Connection expired.");
        send("Terminate.");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.deregisterConnection(this);
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        gameStarted = true;
        notifyAll();
    }
}