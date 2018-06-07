package it.polimi.ingsw.util;


import it.polimi.ingsw.client.interfaces.CliController;

public class CliInputController implements CliController {

    public boolean nameController(String s) {
        return s.contains(" ");
    }

    public boolean connecionController(String s) {
        if (!s.matches("[0-9]+")) return true;
        else if (Integer.parseInt(s) == 1 || Integer.parseInt(s) == 2) return false;
        else return true;
    }

    public boolean ipController(String s) {

        String[] ctrl = s.split("\\.");
        if (ctrl.length != 4) return true;
        int j;
        for (int i = 0; i < 3; i++) {
            if (!ctrl[i].matches("[0-9]+")) return true;
            j = Integer.parseInt(ctrl[i]);
            if (j < 0 || j > 255) return true;
        }
        return false;
    }

    public boolean portController(String s) {
        if (!s.matches("[0-9]+")) return true;
        else if (Integer.parseInt(s) <= 1024 || Integer.parseInt(s) >= 65535) return true;
        else return false;
    }

    public boolean schemeController(String s){
        if (!s.matches("[1-4]")) return true;
        else if (Integer.parseInt(s) > 0 || Integer.parseInt(s) <= 4) return false;
        else return true;
    }

}
