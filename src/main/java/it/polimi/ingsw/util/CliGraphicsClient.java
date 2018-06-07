package it.polimi.ingsw.util;

import it.polimi.ingsw.client.interfaces.CliOutPut;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class CliGraphicsClient implements CliOutPut {

    public void printGraphics(){
        System.out.println("Please insert 1 if you to play with CLI or 2 if you want to play with GUI");
    }

    public void start() {
        System.out.println("        \033[31;1mWelcome To Sagrada\033[0m");
        System.out.println("More information about the game and the");
        System.out.println("rules of Sagrada at the following link:");
        System.out.println(" http://www.craniocreations.it/prodotto/sagrada/");
        System.out.println("\n");
    }

    public void insert() {
        System.out.println("Please insert your nickname with no spaces: ");
    }

    public void printConnection() {
        System.out.println("Press 1 to use Socket, 2 to use RMI.");
    }

    public void printChoice(String s) throws IOException, ParseException {
        String substringSchemes = s.substring(s.indexOf(".") + 2);
        System.out.println("Please choose one of these schemes: insert a number between 1 and 4. " + s + "\n");
        String[] choice = substringSchemes.split(",");
        JSONParser parser = new JSONParser();
        for (int i = 0; i < 4; i++) {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/dashboard_client/" + choice[i] + ".json"));
            System.out.println(i + 1 + ") " + "Nome: " + jsonObject.get("Name"));
            System.out.println("Token: " + jsonObject.get("token"));
            System.out.println(jsonObject.get("String"));
            System.out.println("\n");
        }

    }

    public void printChoice( int i){
        System.out.println("Please insert a number between 1 and 4. ");
    }

    public void printGeneric(String s) {
        System.out.println(s);
    }

    public void printPrivate(String s) {
        System.out.println(s + "\033[0m");
    }

    public void printTool(String s) throws IOException, ParseException {
        String substringSchemes = s.substring(s.indexOf(":") + 2);
        String[] tool = substringSchemes.split(",");
        JSONParser parser = new JSONParser();
        System.out.println("\n\u001B[34mTool:\033[0m");
        for (int i = 0; i < 3; i++) {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src/main/resources/tool/" + tool[i] + ".json"));
            System.out.println(i + 1 + ") " + "Nome: " + jsonObject.get("Name"));
            System.out.println(jsonObject.get("String"));
        }
    }

    public void printRulesFirst() {
        System.out.println("\n");
        System.out.println("Premi 1 se vuoi piazzare un dado");
        System.out.println("Premi 2 se vuoi usare una ToolCard");
        System.out.println("Premi 3 se vuoi passare il turno");
        System.out.println("Premi 4 se vuoi lasciare il gioco");
    }

    public void printRulesDash() {
        System.out.println("Inserisci il numero del dado che vuoi prelevare dalla DraftPool - INSERISCI UN NUMERO DA 0 A 8");
    }

    public void printRulesMatrix(){
        System.out.println("Inserisci l'indice della Riga e della Colonna in cui vuoi piazzare il dado");
    }

    public void printIP() {
        System.out.println("Please insert the IP Server's address");
    }

    public void printPort() {
        System.out.println("Please insert your local port, choose a number between 1024 and 65535");
    }

}
