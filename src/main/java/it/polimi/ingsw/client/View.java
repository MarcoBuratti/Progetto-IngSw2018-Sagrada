package it.polimi.ingsw.client;

import it.polimi.ingsw.client.connection.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.connection.socket.SocketConnectionClient;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import it.polimi.ingsw.util.GraphicsClient;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

public abstract class View implements Observer {

    private ClientInterface connectionClient;
    private GraphicsClient graphicsClient;
    private String nickname;
    private String address;
    private String port;
    private String choice;

    private boolean chosenScheme = true;
    private boolean hasChosenScheme = false;
    private boolean toolCtrl = true;
    private String schemes;

    private Stage primaryStage;


    public abstract void start();

    private void showInput(String fromServer) {
        if (fromServer.startsWith("You have logged")) {
            loginSuccess(fromServer);

        } else if (fromServer.startsWith("schemes. ")) {
            showSchemes(fromServer);
        } else if (fromServer.startsWith("The game has started")) {
            startGame(fromServer);


        } else if (fromServer.startsWith("Your private achievement is:")) {

            showPrivateAchievement(fromServer);

        } else if (fromServer.startsWith("Update")) {

            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/" + getNickname() + ".json"));

                String achievement = (String) jsonObject.get("Public Achievements");
                showPublicAchievements(achievement);

                String tool = (String) jsonObject.get("Tools");
                if (toolCtrl) {
                    getConnectionClient().setTool(tool);
                    toolCtrl = false;
                }
                showTools(tool);


                String roundTrack = (String) jsonObject.get("Round track");
                showRoundTrack(roundTrack);


                String draft = (String) jsonObject.get("Draft");
                if (draft != null)
                    showDraftPool(draft);


                String number = (String) jsonObject.get("numberPlayer");
                int player = Integer.parseInt(number);

                for (int i = 0; i < player; i++) {
                    String request = "scheme" + i;
                    String scheme = (String) jsonObject.get(request);
                    showPlayers(scheme);

                }

                endUpdate();

            } catch (IOException | ParseException e) {
                System.err.println(e.toString());
            }
        } else if (fromServer.startsWith("You have chosen the following scheme:")) {
            showAnswer(fromServer);

        } else if (fromServer.startsWith("Please wait, the game will start soon.")) {
            showGenericMessage(fromServer);

        } else if (fromServer.startsWith("You win") || fromServer.startsWith("You lose")) {
            endGame(fromServer);

        } else if (fromServer.startsWith("Player:")) {
            addPlayerToRanking(fromServer);

        } else {
            showGenericMessage(fromServer);

        }

    }

    public abstract void connectionSuccess();

    public abstract void loginSuccess(String s);

    public abstract void errorLogin();

    public abstract void showSchemes(String s);

    public abstract void showAnswer(String s);

    public abstract void showPrivateAchievement(String s);

    public abstract void startGame(String s);

    public abstract void showPublicAchievements(String s);

    public abstract void showTools(String s);

    public abstract void showRoundTrack(String s);

    public abstract void showDraftPool(String s);

    public abstract void showPlayers(String s);

    public abstract void endUpdate();

    public abstract String getAction();

    public abstract String getIndex();

    public abstract String getRowColumn();

    public abstract String getRoundTrack();

    public abstract String getTool();

    public abstract String getPlusOrMinus();

    public abstract String getDieNumber();

    public abstract String getNumber();

    public abstract void showGenericMessage(String s);

    public abstract void terminate(String s);

    public abstract void endGame(String s);

    public abstract void addPlayerToRanking(String s);

    void createConnection() {

        if (choice.equals("SOCKET") || choice.equals("1")) {
            connectionClient = new SocketConnectionClient(this, address, Integer.parseInt(port));

        } else {
            connectionClient = new RmiConnectionClient(this, address, Integer.parseInt(port));
        }

    }

    ClientInterface getConnectionClient() {
        return this.connectionClient;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    void setAddress(String address) {
        this.address = address;
    }

    void setPort(String port) {
        this.port = port;
    }

    void setChoice(String choice) {
        this.choice = choice;
    }

    public synchronized void update(Observable o, Object arg) {

        String fromServer = (String) arg;

        if (fromServer.startsWith("Terminate")) {
            terminate(fromServer);

        } else if (!fromServer.startsWith("*")) {
            if (fromServer.startsWith("You have logged in")) {
                if (fromServer.startsWith("You have logged in again as")) {
                    setHasChosenScheme(true);
                    setChosenScheme();
                } else {
                    setHasChosenScheme(false);
                    setChosenScheme();
                }
                int nicknameStartIndex = fromServer.lastIndexOf(' ') + 1;
                String nickname = fromServer.substring(nicknameStartIndex);
                this.setNickname(nickname);
            } else if (fromServer.startsWith("schemes. ")) {
                schemes = fromServer;
                setHasChosenScheme(false);
            }
            fromServer = fromServer.replace("!", "\n");
            showInput(fromServer);
        } else {


            fromServer = fromServer.substring(1, fromServer.length());
            StringTokenizer strtok = new StringTokenizer(fromServer);
            String key;
            String value;
            JSONObject jsonObject = new JSONObject();
            while (strtok.hasMoreTokens()) {
                key = strtok.nextToken("-");
                value = strtok.nextToken("-");
                jsonObject.put(key, value);
            }
            try (FileWriter up = new FileWriter("src/main/files/" + getNickname() + ".json")) {
                up.write(jsonObject.toJSONString());
            } catch (IOException e) {
                System.out.println(e.toString());
            }

            showInput("Update");

        }


    }

    private synchronized void setChosenScheme() {
        this.chosenScheme = false;
        notifyAll();
    }

    boolean getChosenScheme() {
        return this.chosenScheme;
    }

    synchronized boolean getHasChosenScheme() {
        return this.hasChosenScheme;
    }

    synchronized void setHasChosenScheme(boolean bool) {
        this.hasChosenScheme = bool;
        notifyAll();
    }

    public String getSchemes() {
        return schemes;
    }

    GraphicsClient getGraphicsClient() {
        return graphicsClient;
    }

    void setGraphicsClient(GraphicsClient graphicsClient) {
        this.graphicsClient = graphicsClient;
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }


    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(event -> System.exit(0));

        start();
    }

}