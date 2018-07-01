package it.polimi.ingsw.client;

import it.polimi.ingsw.client.connection.rmi.RmiConnectionClient;
import it.polimi.ingsw.client.connection.socket.SocketConnectionClient;
import it.polimi.ingsw.client.interfaces.ClientInterface;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

public abstract class View implements Observer {

    private ClientInterface connectionClient;
    private String nickname;
    private String address;
    private String port;
    private String choice;

    private boolean chosenScheme = true;
    private boolean hasChosenScheme = false;
    private String schemes;


    public abstract void start();

    public abstract void showInput(String s);

    public abstract void showOutput(String s);

    public abstract String getInput();

    public abstract void setNickname();

    public abstract void setIP();

    public abstract void setPort();

    public abstract void setChoice();

    public abstract void setScheme();

    public abstract void continueToPlay(String s);

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

    public synchronized void update(Observable o, Object arg) {
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
                    System.out.println("eseguito\n");
                } catch (IOException e) {
                    System.out.println(e.toString() );
                }
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

}