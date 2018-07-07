package it.polimi.ingsw.client;


import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;


public class CliView extends View  {
    private BufferedReader bufferedReader;

    private String fromClient;
    private String input;
    private boolean connectionSuccessful = false;
    private boolean inputCtrl;
    private InputController cliController;



    public CliView(InputStreamReader input)  {
        bufferedReader = new BufferedReader(input);
        super.setGraphicsClient( new GraphicsClient());
        cliController = new InputController();
    }

    public void start() {


        setIP();
        setChoice();
        setPort();
        setNickname();
        createConnection();
        if ( connectionSuccessful ) {
            getConnectionClient().handleName(getNickname());
            setScheme();
            super.getConnectionClient().game();
            showOutput(getGraphicsClient().printEnd());
        }
    }

    @Override
    public void connectionSuccess() {
        this.connectionSuccessful = true;
    }

    public void setNickname(){
    try {
        inputCtrl = false;
        String nick;
        do {
            showOutput(getGraphicsClient().askNick());
            nick = bufferedReader.readLine();
            inputCtrl = cliController.nameController(nick);
            if (inputCtrl) showOutput(getGraphicsClient().wrongNick());
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
                showOutput(getGraphicsClient().printIP());
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
                showOutput(getGraphicsClient().printPort());
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
                showOutput(getGraphicsClient().printConnection());
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
                    if (inputCtrl) showOutput(getGraphicsClient().printRequest());
                } while (inputCtrl);
            }catch (IOException e) {
                System.out.println(e.toString());
            }
            super.getConnectionClient().handleScheme(super.getSchemes(), fromClient);
            super.setHasChosenScheme(true);
        }
    }



    @Override
    public void loginSuccess(String s) {
        System.out.println(s);
    }

    @Override
    public void errorLogin() {
        System.out.println(getGraphicsClient().errorConnection());
    }

    @Override
    public void showSchemes(String s){
        getGraphicsClient().printChoice(s);
    }

    @Override
    public void showPrivateAchievement(String s){
        System.out.println(getGraphicsClient().printPrivate(s));
    }

    @Override
    public void startGame(String s){System.out.println(getGraphicsClient().printGeneric(s));
    getConnectionClient().setInputControl(true);}

    @Override
    public void showPublicAchievements(String s){
        getGraphicsClient().printAchievements(s);
        }

    @Override
    public void showTools(String s) {
        getGraphicsClient().printTool(s);
    }

    @Override
    public void showRoundTrack(String s) {
        getGraphicsClient().printRoundTrack(s);
    }

    @Override
    public void showDraftPool(String s) {
        getGraphicsClient().printDraft(s);
    }

    @Override
    public void showPlayers(String s) {
        getGraphicsClient().printScheme(s);
    }

    @Override
    public void endUpdate() {
        showOutput(getGraphicsClient().printRulesFirst());
    }

    @Override
    public String getAction() {
        return getInput();
    }

    @Override
    public String getRowColumn() {
        showOutput( getGraphicsClient().printRulesMatrix() );
        return getInput();
    }

    @Override
    public String getRoundTrack() {
        showOutput( getGraphicsClient().printRoundDie() );
        return getInput();

    }

    @Override
    public String getTool() {
        showOutput( getGraphicsClient().printToolIndex() );
        return getInput();
    }

    @Override
    public String getPlusOrMinus() {
        showOutput( getGraphicsClient().printPlusMin() );
        return getInput();
    }

    @Override
    public String getDieNumber() {
        showOutput( getGraphicsClient().printGoOn() );
        return getInput();

    }

    @Override
    public String getNumber() {
        showOutput(getGraphicsClient().printDieNum());
        return getInput();
    }

    @Override
    public void showGenericMessage(String fromServer) {
        System.out.println(getGraphicsClient().printGeneric(fromServer));
    }

    @Override
    public void newGame(String s) {
        getConnectionClient().setInputControl(true);

    }

    @Override
    public void terminate(String fromServer) {
        showOutput(fromServer);
    }


    @Override
    public void showAnswer(String fromServer) {
        System.out.println(getGraphicsClient().printGeneric(fromServer));
    }

    @Override
    public String getIndex() {
        showOutput( getGraphicsClient().printRulesDash() );
        return getInput();
    }



    public void showOutput(String s){
        System.out.println(s);
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