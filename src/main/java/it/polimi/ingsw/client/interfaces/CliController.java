package it.polimi.ingsw.client.interfaces;

public interface CliController {
    boolean nameController(String s);
    boolean connecionController(String s);
    boolean ipController(String s);
    boolean portController(String s);
    boolean schemeController(String s);
}



/*
            if (!hasChosenScheme) {
                inputCtrl = true;
                do {
                    fromClient = bufferedReader.readLine();
                    inputCtrl = cliController.schemeController(fromClient);
                }while (inputCtrl);
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }


            if(!hasChosenScheme) {
                String fromClient = bufferedReader.readLine();
                connectionClient.handleScheme(schemes, fromClient);
                hasChosenScheme = true;
            }
* */