package it.polimi.ingsw.client;

import it.polimi.ingsw.client.interfaces.CliController;
import it.polimi.ingsw.client.interfaces.GraphicsInterface;
import it.polimi.ingsw.util.CliGraphicsClient;
import it.polimi.ingsw.util.CliInputController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    private static boolean inputCtrl;
    private static String choice;
    private static int choiceInt;

    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(input);
        CliController cliController = new CliInputController();
        GraphicsInterface graphicsInterface = new CliGraphicsClient();

        do {
            graphicsInterface.printGraphics();
            choice = bufferedReader.readLine();
            inputCtrl = cliController.connectionController(choice);
        } while (inputCtrl);
        choiceInt  = Integer.parseInt(choice);


        if(choiceInt == 1) {
            View view = new View(new InputStreamReader(System.in));
            view.start();
        }

    }
}