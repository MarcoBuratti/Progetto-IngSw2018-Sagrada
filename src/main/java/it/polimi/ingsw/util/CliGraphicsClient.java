package it.polimi.ingsw.util;

import it.polimi.ingsw.client.interfaces.GraphicsInterface;


public class CliGraphicsClient implements GraphicsInterface {

    public void start(){
        System.out.println("        \033[31;1mWelcome To Sagrada\033[0m");
        System.out.println("More information about the game and the");
        System.out.println("rules of Sagrada at the following link:");
        System.out.println(" http://www.craniocreations.it/prodotto/sagrada/");
        System.out.println("Press 1 if you want to place a die");
        System.out.println("Press 2 if you want to use a tool");
        System.out.println("Press 3 if you want to go through");
        System.out.println("Press 4 if you want to quit the game");
        System.out.println("\n");

    }

    public void insert() {
        System.out.println("Please insert your nickname: ");
    }

    public void printConnection(){
        System.out.println("Press 1 to use Socket, 2 to use RMI.");
    }

    public void printGeneric(String s){
        System.out.println(s);
    }

}
