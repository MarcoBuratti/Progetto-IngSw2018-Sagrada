package it.polimi.ingsw.client;

import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;

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
        InputController cliController = new InputController();
        GraphicsClient cliOutPut = new GraphicsClient();

        do {
            cliOutPut.printStart();
            choice = bufferedReader.readLine();
            inputCtrl = cliController.connectionController(choice);
        } while (inputCtrl);
        choiceInt  = Integer.parseInt(choice);


        if(choiceInt == 1) {
            CliView cliView = new CliView(new InputStreamReader(System.in));
            cliView.start();
        }

    }
}