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
    private GraphicsClient graphicsClient ;
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

    public void showInput(String fromServer) {
        System.out.println("showinput");
        if (fromServer.startsWith("You have logged in")) {
            loginSuccess(fromServer);
        }
        else if (fromServer.startsWith("schemes. "))
            showSchemes(fromServer);

        else if (fromServer.startsWith("The game has started")) {
            System.out.println("instartgame");
            startGame(fromServer);


        }
        else if (fromServer.startsWith("Terminate"))
            System.out.println(graphicsClient.printContinue());
        else if (fromServer.startsWith("Your private achievement is:"))
            showPrivateAchievement(fromServer);

        else if (fromServer.startsWith("Update")) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/up.json"));

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
                if(draft != null)
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
        }
        else {
            System.out.println(graphicsClient.printGeneric(fromServer));
        }

    }

    public abstract void showOutput(String s);

    public abstract String getInput();

    public abstract void setScheme();

    public abstract void continueToPlay(String s);



    public abstract void loginSuccess(String s);

    public abstract void showSchemes(String s);

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




    public void createConnection(){

        if (choice.equals("SOCKET") || choice.equals("1")) {
            connectionClient = new SocketConnectionClient(this, address, Integer.parseInt(port));
            this.connectionClient.handleName(nickname);
            /*synchronized (this){
                try {
                    wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }*/
        } else{
            connectionClient = new RmiConnectionClient(this, address, Integer.parseInt(port));
            this.connectionClient.handleName(nickname);
        }

    }

    public ClientInterface getConnectionClient() {
        return this.connectionClient;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setGraphicsClient(GraphicsClient graphicsClient) {
        this.graphicsClient = graphicsClient;
    }

    public synchronized void update(Observable o, Object arg) {
        System.out.println("update");
            String fromServer = (String) arg;
            if(fromServer.startsWith("Terminate")){
                showOutput("Terminate");
                connectionClient.setContinueToPlay(true);
            }
            else if(!fromServer.startsWith("*")){
                if (fromServer.startsWith("You have logged in")) {
                    if (fromServer.startsWith("You have logged in again as")) {
                        setHasChosenScheme(true);
                        setChosenScheme(false);
                    }
                    else {
                        setHasChosenScheme(false);
                        setChosenScheme(false);
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
            }else if(fromServer.startsWith("*")){
                fromServer = fromServer.substring(1, fromServer.length());
                StringTokenizer strtok = new StringTokenizer(fromServer);
                String key, value;
                JSONObject jsonObject = new JSONObject();
                while (strtok.hasMoreTokens()){
                    key = strtok.nextToken("-");
                    value = strtok.nextToken("-");
                    jsonObject.put(key, value);
                }
                try (FileWriter up = new FileWriter("src/main/files/up.json")) {
                    up.write(jsonObject.toJSONString());
                } catch (IOException e) {
                    System.out.println(e.toString() );
                }
                System.out.println("ciao");
                showInput("Update");
            }else
                showInput(fromServer);
    }

    public synchronized void setHasChosenScheme(boolean bool) {
        this.hasChosenScheme = bool;
        notifyAll();
    }

    public synchronized void setChosenScheme(boolean bool){
        this.chosenScheme = bool;
        notifyAll();
    }

    public boolean getChosenScheme() {
        return this.chosenScheme;
    }

    public boolean getHasChosenScheme() { return this.hasChosenScheme; }

    public String getSchemes(){ return schemes;}

    public GraphicsClient getGraphicsClient() {
        return graphicsClient;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void start(Stage primaryStage){
        this.primaryStage=primaryStage;
        start();
    }

}