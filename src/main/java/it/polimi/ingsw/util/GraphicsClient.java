package it.polimi.ingsw.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class GraphicsClient {

    public void printStart() {
        System.out.println("        \033[31;1mBenvenuto su Sagrada\033[0m");
        System.out.println("Maggiori Informazioni riguardo al gioco");
        System.out.println("e le regole di Sagrada al seguente link:");
        System.out.println(" http://www.craniocreations.it/prodotto/sagrada/");
        System.out.println("\n\n");
        System.out.println("Per favore inserisci 1 per giocare con CLI o 2 per giocare con GUI:");
    }

    public String askNick() {
        return ("Nickname: ");
    }

    public String wrongNick(){ return ("Senza spazio!"); }

    public String printConnection() {
        return ("Inserisci 1 per socket, 2 per RMI");
    }

    public void printChoice(String s) throws IOException, ParseException {
        String substringSchemes = s.substring(s.indexOf(".") + 2);
        System.out.println(printRequest() + s + "\n");
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

    public String printRequest(){
        return ("Per favore scegli uno dei seguenti schemi, inserisci un mìnumero da 1 a 4 ");
    }

    public String printGeneric(String s) {
        return (s);
    }

    public String printPrivate(String s) {
        return (s + "\033[0m");
    }

    public String printEnd(){ return ("Fine del Gioco!");}

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

    public String printRulesFirst() {
        return ("\nPremi 1 se vuoi piazzare un dado\nPremi 2 se vuoi usare una ToolCard\nPremi 3 se vuoi passare il turno\nPremi 4 se vuoi lasciare il gioco");
    }

    public String printRulesDash() {
        return ("Inserisci il numero del dado che vuoi prelevare dalla DraftPool - INSERISCI UN NUMERO DA 0 A 8");
    }

    public String printRulesMatrix(){
        return ("Inserisci l'indice della Riga e della Colonna in cui vuoi piazzare il dado");
    }

    public String printIP() {
        return ("Per favore inserisci l'indirizzo IP del server");
    }

    public String printPort() {
        return ("Per favore inserisci la tua localPort, scegli un numero da 1024 a 65535");
    }

}
