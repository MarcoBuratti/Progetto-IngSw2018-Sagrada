package it.polimi.ingsw.util;

public class CliGraphicsServer {

    public void printStart(){
        System.out.println("Game started!");
    }

    public void printLogged(String s){
        System.out.println(s + " has logged in again.");
    }

    public void printErr(){
        System.out.println("Client Connection Error!");
    }

    public void printLoggedIn(String s){
        System.out.println(s + " has logged in.");
    }

    public void printDereg(String s){
        System.out.println(s + " has disconnected from the server.");
    }
}
