package it.polimi.ingsw.client;


import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;


public class CliView extends View  {
    private BufferedReader bufferedReader;

    private GraphicsClient graphicsClient;
    private String fromClient;
    private String input;
    private boolean inputCtrl;
    private boolean toolCtrl = true;
    private boolean wantToPlay = true;
    private InputController cliController;
    private JSONParser parser;



    public CliView(InputStreamReader input)  {
        bufferedReader = new BufferedReader(input);
        graphicsClient = new GraphicsClient();
        cliController = new InputController();
        parser = new JSONParser();
    }

    public void start() {


        setIP();
        setChoice();
        setPort();
        setNickname();
        while (wantToPlay) {
            createConnection();
            setScheme();
            super.getConnectionClient().game();
        /*if (!super.getHasChosenScheme()) {
            setScheme();
            super.getConnectionClient().handleScheme(super.getSchemes(), fromClient);
            super.setHasChosenScheme(true);
        }*/

        }
        showOutput(graphicsClient.printEnd());
    }

    public void setNickname(){
    try {
        inputCtrl = false;
        String nick;
        do {
            showOutput(graphicsClient.askNick());
            nick = bufferedReader.readLine();
            inputCtrl = cliController.nameController(nick);
            if (inputCtrl) showOutput(graphicsClient.wrongNick());
        } while (inputCtrl);
        super.setNickname(nick);
    }catch (IOException e) {
        System.err.println(e.toString());
    }
    }

    public void setIP() {
        try {
            String address;
            do {
                showOutput(graphicsClient.printIP());
                address = bufferedReader.readLine();
                inputCtrl = cliController.ipController(address);
            } while (inputCtrl);
            super.setAddress(address);
        }catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void setPort() {
        try {
            inputCtrl = true;
            String port;
            do {
                showOutput(graphicsClient.printPort());
                port = bufferedReader.readLine();
                inputCtrl = cliController.portController(port);
            } while (inputCtrl);
            super.setPort(port);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void setChoice() {
        try {
            String choice;
            inputCtrl = true;
            do {
                showOutput(graphicsClient.printConnection());
                choice = bufferedReader.readLine();
                inputCtrl = cliController.connectionController(choice);
            } while (inputCtrl);
            super.setChoice(choice);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void setScheme() {
        synchronized ( this ) {
            while (super.getChosenScheme()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!super.getHasChosenScheme()) {
            try {
                inputCtrl = true;
                do {
                    fromClient = bufferedReader.readLine();
                    inputCtrl = cliController.schemeController(fromClient);
                    if (inputCtrl) showOutput(graphicsClient.printRequest());
                } while (inputCtrl);
            }catch (IOException e) {
                System.out.println(e.toString());
            }
            super.getConnectionClient().handleScheme(super.getSchemes(), fromClient);
            super.setHasChosenScheme(true);
        }
    }

    public void continueToPlay(String choose){
            inputCtrl = true;
            do {
                showOutput(graphicsClient.printContinue());
                inputCtrl = cliController.continueToPlayController(choose);
            } while (inputCtrl);
        if(choose.equals("1")) {
            setWantToPlay(true);
        }else
            setWantToPlay(false);
    }

    public void showInput(String fromServer) {
            if (fromServer.startsWith("You have logged in")) {
                System.out.println(graphicsClient.printGeneric(fromServer));
            }
            else if (fromServer.startsWith("schemes. "))
                graphicsClient.printChoice(fromServer);
            else if (fromServer.startsWith("The game has started")) {
                System.out.println(graphicsClient.printGeneric(fromServer));
            }
            else if (fromServer.startsWith("Terminate"))
                System.out.println(graphicsClient.printContinue());
            else if (fromServer.startsWith("Your private achievement is:"))
                System.out.println(graphicsClient.printPrivate(fromServer));
            else if (fromServer.startsWith("Update")) {

                try {
                    JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/files/up.json"));
                    String achievement = (String) jsonObject.get("Public Achievements");
                    graphicsClient.printAchievements(achievement);
                    String tool = (String) jsonObject.get("Tools");
                    if (toolCtrl) {
                        super.getConnectionClient().setTool(tool);
                        toolCtrl = false;
                    }
                    graphicsClient.printTool(tool);
                    String roundTrack = (String) jsonObject.get("Round track");
                    graphicsClient.printRoundTrack(roundTrack);
                    String draft = (String) jsonObject.get("Draft");
                    if(draft != null) graphicsClient.printDraft(draft);
                    String number = (String) jsonObject.get("numberPlayer");
                    int player = Integer.parseInt(number);
                    System.out.println(number);
                    for (int i = 0; i < player; i++) {
                        String request = "scheme" + i;
                        System.out.println(request);
                        String scheme = (String) jsonObject.get(request);
                        graphicsClient.printScheme(scheme);
                    }
                    System.out.println(graphicsClient.printRulesFirst());
                } catch (IOException | ParseException e) {
                    System.out.println(fromServer);
                    showInput(fromServer);
                }
            }
            else {
                System.out.println(graphicsClient.printGeneric(fromServer));
            }

    }

    public void showOutput(String s){
        System.out.println(s);
    }

    public void setWantToPlay(boolean wantToPlay) {
        this.wantToPlay = wantToPlay;
    }

    public String getInput(){
        try {
            input = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}