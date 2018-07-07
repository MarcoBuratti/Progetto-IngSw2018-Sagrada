package it.polimi.ingsw.client;


import it.polimi.ingsw.util.GraphicsClient;
import it.polimi.ingsw.util.InputController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client extends Application {
    private static String choice;
    private static View view;

    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(input);
        InputController cliController = new InputController();
        GraphicsClient cliOutPut = new GraphicsClient();

        do {
            cliOutPut.printStart();
            choice = bufferedReader.readLine();
        } while (cliController.connectionController(choice));


        if (choice.equals("1")) {
            view = new CliView(new InputStreamReader(System.in));
            view.start();
        } else Application.launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        view = new GUIView();
        Platform.runLater(() -> view.start(primaryStage));
    }
}