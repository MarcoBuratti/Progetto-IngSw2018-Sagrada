package it.polimi.ingsw.util;

public class CliGraphicsServer {

    /**
     * Prints a String on the server communicating that a new game has started.
     */
    public void printStart() {
        System.out.println("Game started!");
    }

    /**
     * Prints a String on the server communicating that the player having s as nickname has logged in again.
     * @param s the player's nickname
     */
    public void printLoggedAgain(String s) {
        System.out.println(s + " has logged in again.");
    }

    /**
     * Prints a String on the server communicating that a connection error has occurred.
     */
    public void printErr() {
        System.out.println("Client ConnectionClient Error!");
    }

    /**
     * Prints a String on the server communicating that a new player having s as nickname has logged in for the first time.
     * @param s the player's nickname
     */
    public void printLoggedIn(String s) {
        System.out.println(s + " has logged in.");
    }

    /**
     * Prints a String on the server communicating that the player having s as nickname has disconnected from the server.
     * @param s the player's nickname
     */
    public void printDereg(String s) {
        System.out.println(s + " has disconnected from the server.");
    }

    /**
     * Prints a String on the server communicating that the player having s as nickname is the winner of his game.
     * @param s the player's nickname
     */
    public void printWinner(String s) {
        System.out.println(s + " is the winner!");
    }
}
