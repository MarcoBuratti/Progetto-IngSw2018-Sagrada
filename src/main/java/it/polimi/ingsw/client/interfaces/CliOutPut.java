package it.polimi.ingsw.client.interfaces;

import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CliOutput {

    void printGraphics();
    void start();
    void insert();
    void printConnection();
    void printGeneric(String s);
    void printPrivate(String s);
    void printTool(String s) throws FileNotFoundException, IOException, ParseException;
    void printRulesFirst();
    void printRulesDash();
    void printRulesMatrix();
    void printChoice(String s) throws IOException, ParseException;
    void printChoice(int i);
    void printIP();
    void printPort();
}
