package it.polimi.ingsw.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class GraphicsClient {
    private JSONParser parser;

    public GraphicsClient (){
        parser = new JSONParser();
    }

    public void printStart() {
        System.out.println("        \033[31;1mBenvenuto su Sagrada\033[0m");
        System.out.println("Maggiori Informazioni riguardo al gioco");
        System.out.println("e le regole di Sagrada al seguente link:");
        System.out.println(" http://www.craniocreations.it/prodotto/sagrada/");
        System.out.println("\n\n");
        System.out.println("\u001b[1mPer favore inserisci 1 per giocare con CLI o 2 per giocare con GUI:\u001b[0m");
    }

    public String askNick() {
        return ("Nickname: ");
    }

    public String wrongNick() { return ("Senza spazio!"); }

    public String printConnection() {
        return ("Inserisci 1 per socket, 2 per RMI");
    }

    public String printRequest(){
        return ("Per favore scegli uno dei seguenti schemi, inserisci un numero da 1 a 4 ");
    }

    public String printGeneric(String s) {
        return ( "\u001b[1m" + s + "\u001b[0m");
    }

    public String printPrivate(String s) {
        return ("\n" + s + "\u001b[0m");
    }

    public String printContinue(){ return ("se vuoi continuare a giocare premi 1 altrimenti 0"); }

    public String printEnd(){ return ("Fine del Gioco!");}

    public String printRulesFirst() {
        return ("\n\u001b[1mPremi 1 se vuoi piazzare un dado\nPremi 2 se vuoi usare una ToolCard\nPremi 3 se vuoi passare il turno\nPremi 4 se vuoi lasciare il gioco\u001b[0m");
    }

    public String printRulesDash() {
        return ("Inserisci il numero del dado che vuoi prelevare dalla DraftPool - INSERISCI UN NUMERO DA 0 A 8");
    }

    public String printRulesMatrix(){
        return ("Inserisci l'indice della Riga e della Colonna in cui vuoi piazzare il dado");
    }

    public String printToolIndex(){ return ("Scegli uno dei tre Tool, inserisci 1, 2 o 3"); }

    public String printIP() {
        return ("Indirizzo IP:");
    }

    public String printPort() {
        return ("Porta:");
    }

  //  public String printToolParam(){ return ( "Inserisci i parametri del Tool");}

    public String printPlusMin(){ return  ("Metti 0 per diminuire 1 per aumentare"); }

    public String printDieNum(){ return  ("Inserisci un numero per il dado da settare, da 1 a 6"); }

    public String printRoundDie(){ return ("Inserisci RoundTrack -1/10- e insersci indice del dado -0/8- "); }

    public void printChoice(String s) {
        String substringSchemes = s.substring(s.indexOf(".") + 2);
        System.out.println(printRequest() + "\n");
        String[] choice = substringSchemes.split(",");
        for (int i = 0; i < 4; i++) {
            ParserScheme parserScheme = new ParserScheme(choice[i]);
            System.out.println(i + 1 + ") " + "Nome: " + parserScheme.getName());
            System.out.println("Token: " + parserScheme.getToken());
            System.out.println(parserScheme.getStringScheme());
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(tool[i+1].equals("true")){
                System.out.println("\u001b[1mIl Tool è stato usato\u001b[0m");
            }else
                System.out.println("\u001b[1mIl Tool non è stato usato\u001b[0m");

            System.out.println("\u001b[1m" + (k) + ") " + "Nome: " + jsonObject.get("Name"));
            System.out.println( jsonObject.get("String") + "\u001b[0m");
        }
    }

    public void printDraft(String s){
        StringBuilder bld = new StringBuilder();

        bld.append("\n\u001B[34mNumber of the die on the DraftPool: 0  1  2  3  4  5  6  7  8\u001b[0m\n");
        bld.append("                                    " + s);
        System.out.println(bld.toString());
    }

    public void printAchievements(String s){
        s = s.replace("!", "\n");
        System.out.println("\n\u001B[34mPublic Achievements:\u001b[0m");
        System.out.println("\u001b[1m" + s + "\u001b[0m");
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
        return "Inserisci 1 per muovere un altro dado, 0 per inviare la tua mossa.";
    }
}
