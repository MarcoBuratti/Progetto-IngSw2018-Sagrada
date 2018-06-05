package it.polimi.ingsw.client.interfaces;

import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface GraphicsInterface {
    void start();
    void insert();
    void printConnection();
    void printGeneric(String s);
    void printPrivate(String s);
    void printTool(String s) throws FileNotFoundException, IOException, ParseException;
    void printRules();
    void printChoice(String s) throws IOException, ParseException;
    void printIP();
    void printPort();
}
