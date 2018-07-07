package it.polimi.ingsw.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class GraphicsClient {
    private JSONParser parser;

    /**
     * Creates a new GraphicsClient Object and initialize the parser.
     */
    public GraphicsClient (){
        parser = new JSONParser();
    }

    /**
     * Prints the messages that the game launches at the start of the game
     */
    public void printStart() {
        System.out.println("        \033[31;1mWelcome to Sagrada!\033[0m");
        System.out.println("More information about the game and rules");
        System.out.println("are available at the following link:");
        System.out.println("http://www.craniocreations.it/prodotto/sagrada/");
        System.out.println("\n\n");
        System.out.println("\u001b[1mPlease insert 1 to play using CLI or 2 to play using GUI:\u001b[0m");
    }

    public String askNick() {
        return ("Nickname: ");
    }

    public String wrongNick() { return ("Please don't use spaces!"); }

    public String errorConnection(){return ("An error occurred while trying to create a connection. Please try again.");}

    public String printConnection() {
        return ("Insert 1 to play using Socket connection or 2 to play using Rmi connection.");
    }

    public String printRequest(){
        return ("Please choose one of the following schemes, insert a number between 1 and 4.");
    }

    public String printGeneric(String s) {
        return ( "\u001b[1m" + s + "\u001b[0m");
    }

    public String printPrivate(String s) {
        return ("\n" + s + "\u001b[0m");
    }

    public String printEnd(){ return ("Game end!");}

    public String printRulesFirst() {
        return ("\n\u001b[1mInsert 1 if you want to place a die.\nInsert 2 to use a ToolCard.\nInsert 3 to go through.\nInsert 4 to leave the game.\u001b[0m");
    }

    public String printRulesDash() {
        return ("Insert the index of the die you want to get from the draft pool - Insert a number between 0 and 8.");
    }

    public String printRulesMatrix(){
        return ("Please insert the indexes of the Row and the Column where you want to place your die.");
    }

    public String printToolIndex(){ return ("Please choose one of the three ToolCards, Insert 1, 2 or 3."); }

    public String printIP() {
        return ("Server IP Address:");
    }

    public String printPort() {
        return ("Server Port:");
    }

    public String printPlusMin(){ return  ("Please insert 0 to decrease the value of the die or 1 to increase it."); }

    public String printDieNum(){ return  ("Please choose the value of the die, Insert a number between 1 and 6."); }

    public String printRoundDie(){ return ("Please choose the round you want to get your die from - 1-10 - and insert the index of the die you want to choose - 0-8 ."); }

    public void printChoice(String s) {
        String substringSchemes = s.substring(s.indexOf('.') + 2);
        System.out.println(printRequest() + "\n");
        String[] choice = substringSchemes.split(",");
        for (int i = 0; i < 4; i++) {
            SchemeParser schemeParser = new SchemeParser(choice[i]);
            System.out.println(i + 1 + ") " + "Name: " + schemeParser.getName());
            System.out.println("Favour tokens: " + schemeParser.getToken());
            System.out.println(schemeParser.getStringScheme());
        }

    }

    public void printTool(String s) {

        String[] tool = s.split(",");
        System.out.println("\u001B[34mTool:\033[0m");
        int k = 1;
        for (int i = 0; i < tool.length; i = i + 2, k++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/tool/" + tool[i] + ".json"));
                if(tool[i+1].equals("true")){
                    System.out.println("\u001b[1mThe following ToolCard has already been used once:\u001b[0m");
                }else
                    System.out.println("\u001b[1mThe following ToolCard has never been used:\u001b[0m");

                System.out.println("\u001b[1m" + (k) + ") " + "Name: " + jsonObject.get("Name"));
                System.out.println( jsonObject.get("String") + "\u001b[0m");
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void printDraft(String s){

        String bld = "\n\u001B[34mNumber of the die on the DraftPool: 0  1  2  3  4  5  6  7  8\u001b[0m\n" +
                "                                    " + s;
        System.out.println(bld);
    }

    public void printAchievements(String s){
        String[] achievements = s.split( ",");
        System.out.println("\n\u001B[34mPublic Achievements:\u001b[0m");
        //System.out.println("\u001b[1m" + s + "\u001b[0m");

        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/public_achievement/achievement.json"));
            for (int i = 0; i < 3; i++)
                System.out.println("\u001b[1m" + jsonObject.get(achievements[i]) + "\u001b[0m");
        }

        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public void printScheme(String s){
        String row [] = s.split("!");
        System.out.println("\u001b[1m" + row[0]);
        System.out.println(row[1] + "\u001b[0m");
        System.out.println("     0     1     2     3     4");
        for (int i = 2; i < row.length; i++) {
            System.out.println((i-2) + "  " + row[i]);
        }
    }

    public void printRoundTrack(String s){
        System.out.println("\n\u001B[34mRoundTrack: 0  1  2  3  4  5  6  7  8\033[0m");
        s = s.replace("!", "\n");
        System.out.println(s);
    }

    public String printGoOn() {
        return "Please choose 1 to move another die or 0 to stop and send your move";
    }

    public void printTerminate() {
        System.out.println("Terminate.\nYou have been disconnected.");
    }
}
