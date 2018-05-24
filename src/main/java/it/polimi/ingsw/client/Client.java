package it.polimi.ingsw.client;

import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args){
        View view = new View(new InputStreamReader(System.in));
        view.start();
    }
}