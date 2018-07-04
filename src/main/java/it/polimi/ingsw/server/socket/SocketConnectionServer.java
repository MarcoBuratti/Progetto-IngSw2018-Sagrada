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

    /**
     * Creates a new server thread associated with the socket client connection.
     * @param socket the socket used to communicate with the client
     * @param server the server
     * @throws IOException if it's impossible to get the input stream and/or the output stream from the socket
     */
    public SocketConnectionServer(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        isOn = true;
    }

    /**
     * Sends to the client a message communicating that the player has been disconnected and sets the isOn attribute as false.
     */
    private synchronized void setOff() {
        send("You've been disconnected successfully.");
        this.isOn = false;
    }

    /**
     * Writes on a json file the content of the String jsonContent.
     * @param jsonContent a message received from client that needs to be written on a json file
     */
    private synchronized void read(String jsonContent) {
        StringTokenizer strtok = new StringTokenizer(jsonContent);
        String key;
        JSONObject jsonObject = new JSONObject();
        while (strtok.hasMoreTokens()) {
            key = strtok.nextToken();
            String value = strtok.nextToken();
            jsonObject.put(key, value);
        }
        try (FileWriter lastPlayerMove = new FileWriter("src/main/files/LastPlayerMove.json")) {
            lastPlayerMove.write(jsonObject.toJSONString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Private method used to send the schemes to the player, waiting for his answer.
     * @param schemes the extracted schemes
     * @return the player's choice
     * @throws IOException due to input stream problems
     */
    private String askForChosenScheme(String schemes) throws IOException {
        this.send("schemes. " + schemes);
        return in.readLine();
    }

    /**
     * Sets a default scheme before sending to the player the extracted scheme in order to set a scheme
     * in case the player makes his choice too late.
     * @param schemes the String containing the schemes
     * @return the name of the default scheme
     */
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

    /**
     * Locks the thread waiting for the attribute gameStarted to be set as true.
     */
    private synchronized void waitGameStart() {
        while (!gameStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            this.player = new Player(in.readLine());
            boolean firstLog = !server.alreadyLoggedIn(this);

            if ( firstLog ) {
                server.registerConnection(this);
                waitGameStart();
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
                    send("Trying to make the move ...");
                    setChanged();
                    notifyObservers(newMove);
                }
            }
        } catch (IOException | NotValidValueException e) {
            server.deregisterConnection(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setGame(Game game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {

        setOff();
        send("ConnectionClient expired.");
        send("Terminate.");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.deregisterConnection(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void update(Observable o, Object arg) {
        gameStarted = true;
        notifyAll();
    }
}